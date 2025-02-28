package ly.david.musicsearch.data.repository.releasegroup

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import app.cash.paging.insertSeparators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsByEntityRepository
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes

class ReleaseGroupsByEntityRepositoryImpl(
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val browseApi: BrowseApi,
    private val releaseGroupDao: ReleaseGroupDao,
) : ReleaseGroupsByEntityRepository,
    BrowseEntitiesByEntity<ReleaseGroupListItemModel, ReleaseGroupMusicBrainzModel, BrowseReleaseGroupsResponse>(
        browseEntity = MusicBrainzEntity.RELEASE_GROUP,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeReleaseGroupsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        return observeEntitiesByEntity(
            entityId = entityId,
            entity = entity,
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

    override fun deleteLinkedEntitiesByEntity(
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

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String?,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): PagingSource<Int, ReleaseGroupListItemModel> {
        return when {
            entityId == null || entity == null -> {
                error("not possible")
            }

            entity == MusicBrainzEntity.ARTIST -> {
                artistReleaseGroupDao.getReleaseGroupsByArtist(
                    artistId = entityId,
                    query = listFilters.query,
                    sorted = listFilters.sorted,
                )
            }

            entity == MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getReleaseGroupsByCollection(
                    collectionId = entityId,
                    query = listFilters.query,
                    sorted = listFilters.sorted,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override suspend fun browseEntities(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseReleaseGroupsResponse {
        return when (entity) {
            MusicBrainzEntity.ARTIST -> {
                browseApi.browseReleaseGroupsByArtist(
                    artistId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                browseApi.browseReleaseGroupsByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ReleaseGroupMusicBrainzModel>,
    ): Int {
        releaseGroupDao.insertAll(musicBrainzModels)
        return when (entity) {
            MusicBrainzEntity.ARTIST -> {
                artistReleaseGroupDao.insertAll(
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
}
