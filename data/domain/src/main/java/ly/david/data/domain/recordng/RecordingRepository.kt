package ly.david.data.domain.recordng

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.recording.RecordingDao

@Singleton
class RecordingRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingDao: RecordingDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupRecording(recordingId: String): RecordingScaffoldModel {
        val recordingRoomModel = recordingDao.getRecording(recordingId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(recordingId)
        if (recordingRoomModel != null &&
            recordingRoomModel.artistCreditNamesWithEntities.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            return recordingRoomModel.toRecordingScaffoldModel()
        }

        val recordingMusicBrainzModel = musicBrainzApiService.lookupRecording(recordingId)
        recordingDao.withTransaction {
            recordingDao.insertRecordingWithArtistCredits(recordingMusicBrainzModel)
            relationRepository.insertAllRelations(
                entityId = recordingId,
                relationMusicBrainzModels = recordingMusicBrainzModel.relations,
            )
        }
        return lookupRecording(recordingId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupRecording(
            recordingId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
