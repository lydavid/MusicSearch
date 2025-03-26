package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupForRelease
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseGroupListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import lydavidmusicsearchdatadatabase.Release_group
import lydavidmusicsearchdatadatabase.Release_groups_by_entity

interface ReleaseGroupDao : EntityDao {
    fun insertReleaseGroup(releaseGroup: ReleaseGroupMusicBrainzModel)
    fun insertAllReleaseGroups(releaseGroups: List<ReleaseGroupMusicBrainzModel>)
    fun getReleaseGroupForDetails(releaseGroupId: String): ReleaseGroupDetailsModel?
    fun getReleaseGroupForRelease(releaseId: String): ReleaseGroupForRelease?
    fun deleteReleaseGroup(id: String)

    fun insertReleaseGroupsByArtist(
        artistId: String,
        releaseGroupIds: List<String>,
    ): Int
    fun deleteReleaseGroupsByArtist(artistId: String)
    fun observeCountOfReleaseGroupsByArtist(artistId: String): Flow<Int>
    fun getCountOfReleaseGroupsByArtist(artistId: String): Int
    fun getCountOfEachAlbumType(artistId: String): Flow<List<ReleaseGroupTypeCount>>
    fun getReleaseGroupsByArtist(
        artistId: String,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupListItemModel>
}

class ReleaseGroupDaoImpl(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ReleaseGroupDao {
    override val transacter = database.release_groupQueries

    override fun insertReleaseGroup(releaseGroup: ReleaseGroupMusicBrainzModel) {
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

    override fun insertAllReleaseGroups(releaseGroups: List<ReleaseGroupMusicBrainzModel>) {
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
    ) = ReleaseGroupDetailsModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
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

    override fun insertReleaseGroupsByArtist(
        artistId: String,
        releaseGroupIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            releaseGroupIds.forEach { releaseGroupId ->
                transacter.insertOrIgnoreReleaseGroupByEntity(
                    Release_groups_by_entity(
                        entity_id = artistId,
                        release_group_id = releaseGroupId,
                    ),
                )
            }
            releaseGroupIds.size
        }
    }

    override fun deleteReleaseGroupsByArtist(artistId: String) {
        transacter.deleteReleaseGroupsByEntity(entityId = artistId)
    }

    override fun observeCountOfReleaseGroupsByArtist(artistId: String): Flow<Int> =
        transacter.getNumberOfReleaseGroupsByEntity(
            artistId = artistId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    override fun getCountOfReleaseGroupsByArtist(artistId: String): Int =
        transacter.getNumberOfReleaseGroupsByEntity(
            artistId = artistId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    override fun getCountOfEachAlbumType(artistId: String): Flow<List<ReleaseGroupTypeCount>> =
        transacter.getCountOfEachAlbumType(
            artistId = artistId,
            mapper = { primaryType, secondaryTypes, count ->
                ReleaseGroupTypeCount(
                    primaryType = primaryType,
                    secondaryTypes = secondaryTypes,
                    count = count.toInt(),
                )
            },
        )
            .asFlow()
            .mapToList(coroutineDispatchers.io)

    override fun getReleaseGroupsByArtist(
        artistId: String,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleaseGroupsByEntity(
            artistId = artistId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleaseGroupsByEntity(
                artistId = artistId,
                query = "%$query%",
                sorted = sorted,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseGroupListItemModel,
            )
        },
    )
}
