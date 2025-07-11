package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseGroupListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupForRelease
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import lydavidmusicsearchdatadatabase.Release_group
import lydavidmusicsearchdatadatabase.Release_groups_by_entity

interface ReleaseGroupDao : EntityDao {
    fun insertReleaseGroup(releaseGroup: ReleaseGroupMusicBrainzNetworkModel)
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
        entityId: String,
        isCollection: Boolean,
    ): Flow<List<ReleaseGroupTypeCount>>

    fun getReleaseGroups(
        browseMethod: BrowseMethod,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupListItemModel>

    fun observeCountOfReleaseGroups(browseMethod: BrowseMethod): Flow<Int>
}

class ReleaseGroupDaoImpl(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ReleaseGroupDao {
    override val transacter = database.release_groupQueries

    override fun insertReleaseGroup(releaseGroup: ReleaseGroupMusicBrainzNetworkModel) {
        releaseGroup.run {
            transacter.insertReleaseGroup(
                Release_group(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    first_release_date = firstReleaseDate,
                    primary_type = primaryType,
                    primary_type_id = primaryTypeId,
                    secondary_types = secondaryTypes,
                    secondary_type_ids = secondaryTypeIds,
                ),
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
                insertReleaseGroup(releaseGroup)
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
        firstReleaseDate: String,
        disambiguation: String,
        primaryType: String?,
        secondaryTypes: List<String>?,
        lastUpdated: Instant?,
    ) = ReleaseGroupDetailsModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
        lastUpdated = lastUpdated ?: Clock.System.now(),
    )

    override fun getReleaseGroupForRelease(releaseId: String): ReleaseGroupForRelease? =
        transacter.getReleaseGroupForRelease(
            releaseId = releaseId,
            mapper = { id, name, firstReleaseDate, disambiguation, primaryType, _, secondaryTypes, _ ->
                ReleaseGroupForRelease(
                    id = id,
                    name = name,
                    firstReleaseDate = firstReleaseDate,
                    disambiguation = disambiguation,
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
    ) = transacter.getNumberOfReleaseGroupsByEntity(
        entityId = entityId,
        query = "%$query%",
    )

    override fun getCountOfReleaseGroupsByArtist(entityId: String): Int =
        getCountOfReleaseGroupsByEntityQuery(entityId = entityId, query = "")
            .executeAsOne()
            .toInt()

    override fun observeCountOfEachAlbumType(
        entityId: String,
        isCollection: Boolean,
    ): Flow<List<ReleaseGroupTypeCount>> {
        return if (isCollection) {
            transacter.getCountOfEachAlbumTypeByCollection(
                collectionId = entityId,
                mapper = { primaryType, secondaryTypes, count ->
                    ReleaseGroupTypeCount(
                        primaryType = primaryType,
                        secondaryTypes = secondaryTypes,
                        count = count.toInt(),
                    )
                },
            )
        } else {
            transacter.getCountOfEachAlbumType(
                entityId = entityId,
                mapper = { primaryType, secondaryTypes, count ->
                    ReleaseGroupTypeCount(
                        primaryType = primaryType,
                        secondaryTypes = secondaryTypes,
                        count = count.toInt(),
                    )
                },
            )
        }
            .asFlow()
            .mapToList(coroutineDispatchers.io)
    }

    override fun getReleaseGroups(
        browseMethod: BrowseMethod,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllReleaseGroups(
                query = query,
                sorted = sorted,
            )
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entity == MusicBrainzEntity.COLLECTION) {
                getReleaseGroupsByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                    sorted = sorted,
                )
            } else {
                getReleaseGroupsByEntity(
                    entityId = browseMethod.entityId,
                    query = query,
                    sorted = sorted,
                )
            }
        }
    }

    override fun observeCountOfReleaseGroups(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntity.COLLECTION) {
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
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllReleaseGroups(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllReleaseGroups(
                query = "%$query%",
                sorted = sorted,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseGroupListItemModel,
            )
        },
    )

    private fun getReleaseGroupsByEntity(
        entityId: String,
        query: String,
        sorted: Boolean,
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
                sorted = sorted,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseGroupListItemModel,
            )
        },
    )

    private fun getReleaseGroupsByCollection(
        collectionId: String,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleaseGroupsByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleaseGroupsByCollection(
                collectionId = collectionId,
                query = "%$query%",
                sorted = sorted,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseGroupListItemModel,
            )
        },
    )
}
