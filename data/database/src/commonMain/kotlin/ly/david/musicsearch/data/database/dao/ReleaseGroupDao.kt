package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseGroupListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupForRelease
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupSortOption
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import lydavidmusicsearchdatadatabase.Release_groups_by_entity
import kotlin.time.Clock
import kotlin.time.Instant

interface ReleaseGroupDao : EntityDao {
    fun upsertReleaseGroup(
        oldId: String,
        releaseGroup: ReleaseGroupMusicBrainzNetworkModel,
    )

    fun insertAllReleaseGroups(releaseGroups: List<ReleaseGroupMusicBrainzNetworkModel>)
    fun getReleaseGroupForDetails(releaseGroupId: String): ReleaseGroupDetailsModel?
    fun getReleaseGroupForRelease(releaseId: String): ReleaseGroupForRelease?
    fun deleteReleaseGroup(id: String)

    fun insertReleaseGroupsByEntity(
        entityId: String,
        releaseGroupIds: List<String>,
    )

    fun deleteReleaseGroupLinksByEntity(entityId: String)
    fun getCountOfReleaseGroupsByArtist(entityId: String): Int
    fun observeCountOfEachAlbumType(
        browseMethod: BrowseMethod,
    ): Flow<List<ReleaseGroupTypeCount>>

    fun getReleaseGroups(
        browseMethod: BrowseMethod,
        query: String,
        sortOption: ReleaseGroupSortOption,
    ): PagingSource<Int, ReleaseGroupListItemModel>

    fun observeLocalCount(browseMethod: BrowseMethod): Flow<Int>
}

class ReleaseGroupDaoImpl(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ReleaseGroupDao {
    override val transacter = database.release_groupQueries

    override fun upsertReleaseGroup(
        oldId: String,
        releaseGroup: ReleaseGroupMusicBrainzNetworkModel,
    ) {
        releaseGroup.run {
            if (oldId != id) {
                deleteReleaseGroup(oldId)
            }
            transacter.upsert(
                id = id,
                name = name,
                disambiguation = disambiguation,
                first_release_date = firstReleaseDate,
                primary_type = primaryType.orEmpty(),
                primary_type_id = primaryTypeId.orEmpty(),
                secondary_types = secondaryTypes.orEmpty(),
                secondary_type_ids = secondaryTypeIds.orEmpty(),
            )
            artistCreditDao.insertArtistCredits(
                entityId = releaseGroup.id,
                artistCredits = artistCredits,
            )
        }
    }

    override fun insertAllReleaseGroups(releaseGroups: List<ReleaseGroupMusicBrainzNetworkModel>) {
        transacter.transaction {
            releaseGroups.forEach { releaseGroup ->
                upsertReleaseGroup(
                    oldId = releaseGroup.id,
                    releaseGroup = releaseGroup,
                )
            }
        }
    }

    override fun getReleaseGroupForDetails(releaseGroupId: String): ReleaseGroupDetailsModel? =
        transacter.getReleaseGroupForDetails(
            releaseGroupId = releaseGroupId,
            mapper = ::mapToReleaseGroupForDetails,
        ).executeAsOneOrNull()

    private fun mapToReleaseGroupForDetails(
        id: String,
        name: String,
        disambiguation: String,
        firstReleaseDate: String,
        primaryType: String,
        secondaryTypes: List<String>,
        lastUpdated: Instant?,
    ) = ReleaseGroupDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        firstReleaseDate = firstReleaseDate,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes.toPersistentList(),
        lastUpdated = lastUpdated ?: Clock.System.now(),
    )

    override fun getReleaseGroupForRelease(releaseId: String): ReleaseGroupForRelease? =
        transacter.getReleaseGroupForRelease(
            releaseId = releaseId,
            mapper = { id, name, disambiguation, firstReleaseDate, primaryType, _, secondaryTypes, _ ->
                ReleaseGroupForRelease(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    firstReleaseDate = firstReleaseDate,
                    primaryType = primaryType,
                    secondaryTypes = secondaryTypes,
                )
            },
        ).executeAsOneOrNull()

    override fun deleteReleaseGroup(id: String) {
        transacter.deleteReleaseGroup(id)
    }

    override fun insertReleaseGroupsByEntity(
        entityId: String,
        releaseGroupIds: List<String>,
    ) {
        transacter.transaction {
            releaseGroupIds.forEach { releaseGroupId ->
                transacter.insertOrIgnoreReleaseGroupByEntity(
                    Release_groups_by_entity(
                        entity_id = entityId,
                        release_group_id = releaseGroupId,
                    ),
                )
            }
        }
    }

    override fun deleteReleaseGroupLinksByEntity(entityId: String) {
        transacter.deleteReleaseGroupLinksByEntity(entityId = entityId)
    }

    private fun getCountOfReleaseGroupsByEntityQuery(
        entityId: String,
        query: String,
    ) = transacter.getCountOfReleaseGroupsByEntity(
        entityId = entityId,
        query = "%$query%",
    )

    override fun getCountOfReleaseGroupsByArtist(entityId: String): Int =
        getCountOfReleaseGroupsByEntityQuery(entityId = entityId, query = "")
            .executeAsOne()
            .toInt()

    override fun observeCountOfEachAlbumType(
        browseMethod: BrowseMethod,
    ): Flow<List<ReleaseGroupTypeCount>> {
        return when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                    transacter.getCountOfEachAlbumTypesByCollection(
                        collectionId = browseMethod.entityId,
                        mapper = { primaryType, secondaryTypes, count ->
                            ReleaseGroupTypeCount(
                                primaryType = primaryType,
                                secondaryTypes = secondaryTypes,
                                count = count.toInt(),
                            )
                        },
                    )
                } else {
                    transacter.getCountOfEachAlbumTypesByEntity(
                        entityId = browseMethod.entityId,
                        mapper = { primaryType, secondaryTypes, count ->
                            ReleaseGroupTypeCount(
                                primaryType = primaryType,
                                secondaryTypes = secondaryTypes,
                                count = count.toInt(),
                            )
                        },
                    )
                }
            }

            BrowseMethod.All -> {
                transacter.getCountOfEachAlbumTypes(
                    mapper = { primaryType, secondaryTypes, count ->
                        ReleaseGroupTypeCount(
                            primaryType = primaryType,
                            secondaryTypes = secondaryTypes,
                            count = count.toInt(),
                        )
                    },
                )
            }
        }
            .asFlow()
            .mapToList(coroutineDispatchers.io)
    }

    override fun getReleaseGroups(
        browseMethod: BrowseMethod,
        query: String,
        sortOption: ReleaseGroupSortOption,
    ): PagingSource<Int, ReleaseGroupListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllReleaseGroups(
                query = query,
                sortOption = sortOption,
            )
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                getReleaseGroupsByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                    sortOption = sortOption,
                )
            } else {
                getReleaseGroupsByEntity(
                    entityId = browseMethod.entityId,
                    query = query,
                    sortOption = sortOption,
                )
            }
        }
    }

    override fun observeLocalCount(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                    collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getCountOfReleaseGroupsByEntityQuery(
                        entityId = browseMethod.entityId,
                        query = "",
                    )
                }
            }

            is BrowseMethod.All -> {
                getCountOfAllReleaseGroups(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllReleaseGroups(
        query: String,
    ): Query<Long> = transacter.getCountOfAllReleaseGroups(
        query = "%$query%",
    )

    private fun getAllReleaseGroups(
        query: String,
        sortOption: ReleaseGroupSortOption,
    ): PagingSource<Int, ReleaseGroupListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllReleaseGroups(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllReleaseGroups(
                query = "%$query%",
                sortBy = sortOption.ordinal.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseGroupListItemModel,
            )
        },
    )

    private fun getReleaseGroupsByEntity(
        entityId: String,
        query: String,
        sortOption: ReleaseGroupSortOption,
    ): PagingSource<Int, ReleaseGroupListItemModel> = QueryPagingSource(
        countQuery = getCountOfReleaseGroupsByEntityQuery(
            entityId = entityId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleaseGroupsByEntity(
                entityId = entityId,
                query = "%$query%",
                sortBy = sortOption.ordinal.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseGroupListItemModel,
            )
        },
    )

    private fun getReleaseGroupsByCollection(
        collectionId: String,
        query: String,
        sortOption: ReleaseGroupSortOption,
    ): PagingSource<Int, ReleaseGroupListItemModel> = QueryPagingSource(
        countQuery = transacter.getCountOfReleaseGroupsByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleaseGroupsByCollection(
                collectionId = collectionId,
                query = "%$query%",
                sortBy = sortOption.ordinal.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseGroupListItemModel,
            )
        },
    )
}
