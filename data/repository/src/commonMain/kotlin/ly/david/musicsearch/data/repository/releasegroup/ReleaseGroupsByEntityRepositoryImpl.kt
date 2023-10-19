package ly.david.musicsearch.data.repository.releasegroup

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.releasegroup.getDisplayTypes
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupsByEntityRepository

class ReleaseGroupsByEntityRepositoryImpl(
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val musicBrainzApi: MusicBrainzApi,
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
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): PagingSource<Int, ReleaseGroupListItemModel> {
        return when (entity) {
            MusicBrainzEntity.ARTIST -> {
                artistReleaseGroupDao.getReleaseGroupsByArtist(
                    artistId = entityId,
                    query = listFilters.query,
                    sorted = listFilters.sorted,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
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

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ReleaseGroupMusicBrainzModel>,
    ) {
        releaseGroupDao.insertAll(musicBrainzModels)
        when (entity) {
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
