package ly.david.musicsearch.data.repository.release

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.musicbrainz.api.ARTIST_CREDITS
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.api.LABELS
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository

class ReleasesListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseApi: BrowseApi,
    private val releaseDao: ReleaseDao,
) : ReleasesListRepository,
    BrowseEntities<ReleaseListItemModel, ReleaseMusicBrainzNetworkModel, BrowseReleasesResponse>(
        browseEntity = MusicBrainzEntity.RELEASE,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
    ) {

    override fun observeReleases(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ReleaseListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun observeCountOfReleases(browseMethod: BrowseMethod?): Flow<Int> {
        if (browseMethod == null) return flowOf()
        return releaseDao.observeCountOfReleases(browseMethod = browseMethod)
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, ReleaseListItemModel> {
        return releaseDao.getReleases(
            browseMethod = browseMethod,
            query = listFilters.query,
        )
    }

    override fun deleteEntityLinksByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteBrowseRemoteCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.AREA -> {
                    releaseDao.deleteReleasesByCountry(entityId)
                }

                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteEntityLinksFromCollection(entityId)
                }

                MusicBrainzEntity.LABEL -> {
                    releaseDao.deleteReleasesByLabel(entityId)
                }

                else -> releaseDao.deleteReleaseLinksByEntity(entityId)
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseReleasesResponse {
        return when (entity) {
            MusicBrainzEntity.LABEL -> {
                browseApi.browseReleasesByEntity(
                    entityId = entityId,
                    entity = entity,
                    offset = offset,
                    include = "$ARTIST_CREDITS+$LABELS",
                )
            }

            else -> {
                browseApi.browseReleasesByEntity(
                    entityId = entityId,
                    entity = entity,
                    offset = offset,
                )
            }
        }
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ReleaseMusicBrainzNetworkModel>,
    ) {
        releaseDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.AREA -> {
                releaseDao.insertReleasesByCountry(
                    areaId = entityId,
                    releases = musicBrainzModels,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { release -> release.id },
                )
            }

            MusicBrainzEntity.LABEL -> {
                releaseDao.insertReleasesByLabel(
                    labelId = entityId,
                    releases = musicBrainzModels,
                )
            }

            else -> releaseDao.insertReleasesByEntity(
                entityId = entityId,
                releases = musicBrainzModels,
            )
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Int {
        return when (entity) {
            MusicBrainzEntity.AREA -> {
                releaseDao.getCountOfReleasesByCountry(entityId)
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entityId)
            }

            MusicBrainzEntity.LABEL -> {
                releaseDao.getCountOfReleasesByLabel(entityId)
            }

            else -> releaseDao.getCountOfReleasesByEntity(entityId)
        }
    }
}
