package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RecordingScaffoldModel
import ly.david.data.domain.toRecordingScaffoldModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.recording.RecordingDao

@Singleton
class RecordingRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingDao: RecordingDao,
) : RelationsListRepository {

    suspend fun lookupRecording(recordingId: String): RecordingScaffoldModel {
        val recordingRoomModel = recordingDao.getRecordingWithArtistCredits(recordingId)
        if (recordingRoomModel != null && recordingRoomModel.artistCreditNamesWithResources.isNotEmpty()) {
            return recordingRoomModel.toRecordingScaffoldModel()
        }

        val recordingMusicBrainzModel = musicBrainzApiService.lookupRecording(recordingId)
        recordingDao.insertRecordingWithArtistCredits(recordingMusicBrainzModel)
        return recordingMusicBrainzModel.toRecordingScaffoldModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupRecording(
            recordingId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }
}
