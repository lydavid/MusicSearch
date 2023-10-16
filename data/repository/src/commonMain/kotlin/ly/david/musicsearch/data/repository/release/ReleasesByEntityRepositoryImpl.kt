package ly.david.musicsearch.data.repository.release

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.repository.BrowseEntitiesByEntity
import ly.david.musicsearch.domain.release.ReleasesByEntityRepository

class ReleasesByEntityRepositoryImpl(
    private val artistReleaseDao: ArtistReleaseDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val musicBrainzApi: MusicBrainzApi,
    private val recordingReleaseDao: RecordingReleaseDao,
    private val releaseDao: ReleaseDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val releaseLabelDao: ReleaseLabelDao,
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao,
) : ReleasesByEntityRepository,
    BrowseEntitiesByEntity<ReleaseListItemModel, ReleaseMusicBrainzModel, BrowseReleasesResponse>(
        browseEntity = MusicBrainzEntity.RELEASE,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeReleasesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<ReleaseListItemModel>> {
        return observeEntitiesByEntity(
            entityId = entityId,
            entity = entity,
            listFilters = listFilters,
        )
    }

    override fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteBrowseEntityCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.AREA -> {
                    releaseCountryDao.deleteReleasesByCountry(entityId)
                }

                MusicBrainzEntity.ARTIST -> {
                    artistReleaseDao.deleteReleasesByArtist(entityId)
                }

                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                MusicBrainzEntity.LABEL -> {
                    releaseLabelDao.deleteReleasesByLabel(entityId)
                }

                MusicBrainzEntity.RECORDING -> {
                    recordingReleaseDao.deleteReleasesByRecording(entityId)
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    releaseReleaseGroupDao.deleteReleasesByReleaseGroup(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): PagingSource<Int, ReleaseListItemModel> {
        return when (entity) {
            MusicBrainzEntity.AREA -> {
                releaseCountryDao.getReleasesByCountry(
                    areaId = entityId,
                    query = listFilters.query,
                )
            }

            MusicBrainzEntity.ARTIST -> {
                artistReleaseDao.getReleasesByArtist(
                    artistId = entityId,
                    query = listFilters.query,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getReleasesByCollection(
                    collectionId = entityId,
                    query = listFilters.query,
                )
            }

            MusicBrainzEntity.LABEL -> {
                releaseLabelDao.getReleasesByLabel(
                    labelId = entityId,
                    query = listFilters.query,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                recordingReleaseDao.getReleasesByRecording(
                    recordingId = entityId,
                    query = listFilters.query,
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                releaseReleaseGroupDao.getReleasesByReleaseGroup(
                    releaseGroupId = entityId,
                    query = listFilters.query,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override suspend fun browseEntities(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseReleasesResponse {
        return when (entity) {
            MusicBrainzEntity.AREA -> {
                musicBrainzApi.browseReleasesByArea(
                    areaId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.ARTIST -> {
                musicBrainzApi.browseReleasesByArtist(
                    artistId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                musicBrainzApi.browseReleasesByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.LABEL -> {
                musicBrainzApi.browseReleasesByLabel(
                    labelId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                musicBrainzApi.browseReleasesByRecording(
                    recordingId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                musicBrainzApi.browseReleasesByReleaseGroup(
                    releaseGroupId = entityId,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ReleaseMusicBrainzModel>,
    ) {
        releaseDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.AREA -> {
                releaseCountryDao.linkReleasesByCountry(
                    areaId = entityId,
                    releases = musicBrainzModels,
                )
            }

            MusicBrainzEntity.ARTIST -> {
                artistReleaseDao.insertAll(
                    artistId = entityId,
                    releaseIds = musicBrainzModels.map { release -> release.id },
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { release -> release.id },
                )
            }

            MusicBrainzEntity.LABEL -> {
                releaseLabelDao.linkReleasesByLabel(
                    labelId = entityId,
                    releases = musicBrainzModels,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                recordingReleaseDao.insertAll(
                    recordingId = entityId,
                    releaseIds = musicBrainzModels.map { release -> release.id },
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                releaseReleaseGroupDao.insertAll(
                    releaseGroupId = entityId,
                    releaseIds = musicBrainzModels.map { release -> release.id },
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }
}
