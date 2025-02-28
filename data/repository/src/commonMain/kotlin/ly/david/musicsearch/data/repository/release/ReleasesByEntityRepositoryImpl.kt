package ly.david.musicsearch.data.repository.release

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseLabelDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.ARTIST_CREDITS
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.api.LABELS
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.ReleasesByEntityRepository

class ReleasesByEntityRepositoryImpl(
    private val artistReleaseDao: ArtistReleaseDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseApi: BrowseApi,
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
        entityId: String?,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): PagingSource<Int, ReleaseListItemModel> {
        return when {
            entityId == null || entity == null -> {
                error("not possible")
            }

            entity == MusicBrainzEntity.AREA -> {
                releaseCountryDao.getReleasesByCountry(
                    areaId = entityId,
                    query = listFilters.query,
                )
            }

            entity == MusicBrainzEntity.ARTIST -> {
                artistReleaseDao.getReleasesByArtist(
                    artistId = entityId,
                    query = listFilters.query,
                )
            }

            entity == MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getReleasesByCollection(
                    collectionId = entityId,
                    query = listFilters.query,
                )
            }

            entity == MusicBrainzEntity.LABEL -> {
                releaseLabelDao.getReleasesByLabel(
                    labelId = entityId,
                    query = listFilters.query,
                )
            }

            entity == MusicBrainzEntity.RECORDING -> {
                recordingReleaseDao.getReleasesByRecording(
                    recordingId = entityId,
                    query = listFilters.query,
                )
            }

            entity == MusicBrainzEntity.RELEASE_GROUP -> {
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
        musicBrainzModels: List<ReleaseMusicBrainzModel>,
    ): Int {
        releaseDao.insertAll(musicBrainzModels)
        return when (entity) {
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
