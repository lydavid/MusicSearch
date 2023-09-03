package ly.david.data.domain.recordng

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.recording.RecordingDao

@Singleton
class RecordingRepository @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val recordingDao: RecordingDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupRecording(recordingId: String): RecordingScaffoldModel {
        val recordingWithAllData = recordingDao.getRecording(recordingId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(recordingId)
        if (recordingWithAllData != null &&
            recordingWithAllData.artistCreditNamesWithEntities.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            return recordingWithAllData.toRecordingScaffoldModel()
        }

        val recordingMusicBrainzModel = musicBrainzApi.lookupRecording(recordingId)
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
        return musicBrainzApi.lookupRecording(
            recordingId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
