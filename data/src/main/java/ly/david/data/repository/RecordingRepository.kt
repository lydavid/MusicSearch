package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.Recording
import ly.david.data.domain.RecordingUiModel
import ly.david.data.domain.toRecordingUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.history.LookupHistory
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.ReleaseRecording
import ly.david.data.persistence.recording.ReleasesRecordingsDao
import ly.david.data.persistence.recording.toRecordingRoomModel
import ly.david.data.persistence.relation.BrowseResourceOffset
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseWithReleaseCountries
import ly.david.data.persistence.release.toReleaseRoomModel

@Singleton
class RecordingRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingDao: RecordingDao,
    private val relationDao: RelationDao,
    private val releaseDao: ReleaseDao,
    private val lookupHistoryDao: LookupHistoryDao,
    private val releasesRecordingsDao: ReleasesRecordingsDao
) : ReleasesListRepository {

    suspend fun lookupRecording(recordingId: String): RecordingUiModel {

        val recordingRoomModel = recordingDao.getRecording(recordingId)
        if (recordingRoomModel != null) {
            incrementOrInsertLookupHistory(recordingRoomModel)
            return recordingRoomModel.toRecordingUiModel()
        }

        val recordingMusicBrainzModel = musicBrainzApiService.lookupRecording(recordingId)
        recordingDao.insert(recordingMusicBrainzModel.toRecordingRoomModel())

        // TODO: move relations
//        val recordingRelations = mutableListOf<RelationRoomModel>()
//        musicBrainzRecording.relations?.forEachIndexed { index, relationMusicBrainzModel ->
//            relationMusicBrainzModel.toRelationRoomModel(
//                resourceId = recordingId,
//                order = index
//            )?.let { relationRoomModel ->
//                recordingRelations.add(relationRoomModel)
//            }
//        }
//        relationDao.insertAll(recordingRelations)

        incrementOrInsertLookupHistory(recordingMusicBrainzModel)
        return recordingMusicBrainzModel.toRecordingUiModel()
    }

    private suspend fun incrementOrInsertLookupHistory(recording: Recording) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                title = recording.name,
                resource = MusicBrainzResource.RECORDING,
                mbid = recording.id
            )
        )
    }

    override suspend fun browseReleasesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByRecording(
            recordingId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResource(
                browseResourceRoomModel = BrowseResourceOffset(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementOffsetForResource(resourceId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val releaseMusicBrainzModels = response.releases
        releaseDao.insertAll(releaseMusicBrainzModels.map { it.toReleaseRoomModel() })
        releasesRecordingsDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ReleaseRecording(
                    releaseId = release.id,
                    recordingId = resourceId
                )
            }
        )

        return releaseMusicBrainzModels.size
    }

    override suspend fun getRemoteReleasesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceOffset(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    override suspend fun getLocalReleasesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceOffset(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    override suspend fun deleteReleasesByResource(resourceId: String) {
        releasesRecordingsDao.deleteReleasesByRecording(resourceId)
        relationDao.deleteBrowseResourceOffsetByResource(resourceId, MusicBrainzResource.RELEASE)
    }

    override fun getReleasesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseWithReleaseCountries> = when {
        query.isEmpty() -> {
            releasesRecordingsDao.getReleasesByRecording(resourceId)
        }
        else -> {
            releasesRecordingsDao.getReleasesByRecordingFiltered(
                recordingId = resourceId,
                query = "%$query%"
            )
        }
    }
}
