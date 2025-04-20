package ly.david.musicsearch.data.repository.releasegroup

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import app.cash.paging.insertSeparators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.dao.BrowseRemoteCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes

class ReleaseGroupsListRepositoryImpl(
    private val collectionEntityDao: CollectionEntityDao,
    private val browseEntityCountDao: BrowseRemoteCountDao,
    private val browseApi: BrowseApi,
    private val releaseGroupDao: ReleaseGroupDao,
) : ReleaseGroupsListRepository,
    BrowseEntities<ReleaseGroupListItemModel, ReleaseGroupMusicBrainzModel, BrowseReleaseGroupsResponse>(
        browseEntity = MusicBrainzEntity.RELEASE_GROUP,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeReleaseGroups(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        ).map { pagingData ->
            pagingData
                .insertSeparators { rg1: ReleaseGroupListItemModel?, rg2: ReleaseGroupListItemModel? ->
                    if (listFilters.sorted && rg2 != null &&
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

    override fun observeCountOfAllReleaseGroups(): Flow<Long> {
        return releaseGroupDao.observeCountOfAllReleaseGroups()
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, ReleaseGroupListItemModel> {
        return releaseGroupDao.getReleaseGroups(
            browseMethod = browseMethod,
            query = listFilters.query,
            sorted = listFilters.sorted,
        )
    }

    override fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteBrowseRemoteCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.ARTIST -> {
                    releaseGroupDao.deleteReleaseGroupsByArtist(entityId)
                }

                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseReleaseGroupsResponse {
        return browseApi.browseReleaseGroupsByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ReleaseGroupMusicBrainzModel>,
    ) {
        releaseGroupDao.insertAllReleaseGroups(musicBrainzModels)
        return when (entity) {
            MusicBrainzEntity.ARTIST -> {
                releaseGroupDao.insertReleaseGroupsByArtist(
                    artistId = entityId,
                    releaseGroupIds = musicBrainzModels.map { releaseGroup -> releaseGroup.id },
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { releaseGroup -> releaseGroup.id },
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Int {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entityId)
            }

            else -> {
                releaseGroupDao.getCountOfReleaseGroupsByArtist(entityId)
            }
        }
    }
}
