package ly.david.musicsearch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel
import ly.david.musicsearch.core.models.releasegroup.getDisplayTypes
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.relation.RelationRepository
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupRepository
import lydavidmusicsearchdatadatabase.Browse_entity_count

class ReleaseGroupRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val releaseGroupDao: ReleaseGroupDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
) : ReleaseGroupRepository {

    override suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel {
        val releaseGroup = releaseGroupDao.getReleaseGroupForDetails(releaseGroupId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(releaseGroupId)
        val urlRelations = relationRepository.getEntityUrlRelationships(releaseGroupId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(releaseGroupId)
        if (releaseGroup != null &&
            artistCredits.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            return releaseGroup.copy(
                artistCredits = artistCredits,
                urls = urlRelations,
            )
        }

        val releaseGroupMusicBrainzModel = musicBrainzApi.lookupReleaseGroup(releaseGroupId)
        cache(releaseGroupMusicBrainzModel)
        return lookupReleaseGroup(releaseGroupId)
    }

    private fun cache(releaseGroup: ReleaseGroupMusicBrainzModel) {
        releaseGroupDao.withTransaction {
            releaseGroupDao.insert(releaseGroup)

            val relationWithOrderList = releaseGroup.relations.toRelationWithOrderList(releaseGroup.id)
            relationRepository.insertAllUrlRelations(
                entityId = releaseGroup.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun observeReleaseGroupsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        query: String,
        isRemote: Boolean,
        sorted: Boolean,
    ): Flow<PagingData<ListItemModel>> {
        return Pager(
            config = CommonPagingConfig.pagingConfig,
            remoteMediator = getRemoteMediator(
                entityId = entityId,
                entity = entity,
            ).takeIf { isRemote },
            pagingSourceFactory = {
                getLinkedEntitiesPagingSource(
                    entityId = entityId,
                    entity = entity,
                    query = query,
                    sorted = sorted,
                )
            },
        ).flow.map { pagingData ->
            pagingData
                .insertSeparators { rg1: ReleaseGroupListItemModel?, rg2: ReleaseGroupListItemModel? ->
                    if (sorted && rg2 != null &&
                        (rg1?.primaryType != rg2.primaryType || rg1?.secondaryTypes != rg2.secondaryTypes)
                    ) {
                        ListSeparator(
                            id = "${rg1?.id}_${rg2.id}",
                            text = rg2.getDisplayTypes(),
                        )
                    } else {
                        null
                    }
                }
        }
    }

    private fun getRemoteMediator(
        entityId: String,
        entity: MusicBrainzEntity,
    ) = BrowseEntityRemoteMediator<ReleaseGroupListItemModel>(
        getRemoteEntityCount = { getRemoteLinkedEntitiesCountByEntity(entityId) },
        getLocalEntityCount = { getLocalLinkedEntitiesCountByEntity(entityId) },
        deleteLocalEntity = {
            deleteLinkedEntitiesByEntity(
                entityId = entityId,
                entity = entity,
            )
        },
        browseEntity = { offset ->
            browseLinkedEntitiesAndStore(
                entityId = entityId,
                entity = entity,
                nextOffset = offset,
            )
        },
    )

    private fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int? =
        browseEntityCountDao.getBrowseEntityCount(
            entityId = entityId,
            browseEntity = MusicBrainzEntity.RELEASE_GROUP,
        )?.remoteCount

    private fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int =
        browseEntityCountDao.getBrowseEntityCount(
            entityId = entityId,
            browseEntity = MusicBrainzEntity.RELEASE_GROUP,
        )?.localCount ?: 0

    private fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteBrowseEntityCountByEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.RELEASE_GROUP,
            )

            when (entity) {
                MusicBrainzEntity.ARTIST -> {
                    artistReleaseGroupDao.deleteReleaseGroupsByArtist(entityId)
                }

                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> {}
            }
        }
    }

    private fun getLinkedEntitiesPagingSource(
        entityId: String,
        entity: MusicBrainzEntity,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupListItemModel> {
        return when (entity) {
            MusicBrainzEntity.ARTIST -> {
                artistReleaseGroupDao.getReleaseGroupsByArtist(
                    artistId = entityId,
                    query = "%$query%",
                    sorted = sorted,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getReleaseGroupsByCollection(
                    collectionId = entityId,
                    query = "%$query%",
                    sorted = sorted,
                )
            }

            else -> error("Release groups by $entity not supported.")
        }
    }

    private suspend fun browseLinkedEntitiesAndStore(
        entityId: String,
        entity: MusicBrainzEntity,
        nextOffset: Int,
    ): Int {
        val response = browseReleaseGroupsByEntity(
            entityId = entityId,
            entity = entity,
            offset = nextOffset,
        )

        if (response.offset == 0) {
            browseEntityCountDao.insert(
                browseEntityCount = Browse_entity_count(
                    entity_id = entityId,
                    browse_entity = MusicBrainzEntity.RELEASE_GROUP,
                    local_count = response.musicBrainzModels.size,
                    remote_count = response.count,
                ),
            )
        } else {
            browseEntityCountDao.incrementLocalCountForEntity(
                entityId = entityId,
                browseEntity = MusicBrainzEntity.RELEASE_GROUP,
                additionalOffset = response.musicBrainzModels.size,
            )
        }

        val releaseGroupMusicBrainzModels = response.musicBrainzModels
        releaseGroupDao.insertAll(releaseGroupMusicBrainzModels)
        insertAllLinkingModels(
            entityId = entityId,
            entity = entity,
            releaseGroupMusicBrainzModels = releaseGroupMusicBrainzModels,
        )

        return releaseGroupMusicBrainzModels.size
    }

    private suspend fun browseReleaseGroupsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseReleaseGroupsResponse {
        return when (entity) {
            MusicBrainzEntity.ARTIST -> {
                musicBrainzApi.browseReleaseGroupsByArtist(
                    artistId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                musicBrainzApi.browseReleaseGroupsByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            else -> error("Release groups by $entity not supported.")
        }
    }

    private fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        releaseGroupMusicBrainzModels: List<ReleaseGroupMusicBrainzModel>,
    ) {
        when (entity) {
            MusicBrainzEntity.ARTIST -> {
                artistReleaseGroupDao.insertAll(
                    artistId = entityId,
                    releaseGroupIds = releaseGroupMusicBrainzModels.map { releaseGroup -> releaseGroup.id },
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = releaseGroupMusicBrainzModels.map { releaseGroup -> releaseGroup.id },
                )
            }

            else -> {}
        }
    }
}
