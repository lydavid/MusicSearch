package ly.david.musicsearch.domain.recordng

import ly.david.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.domain.RelationsListRepository
import ly.david.musicsearch.domain.relation.RelationRepository
import org.koin.core.annotation.Single

@Single
class RecordingRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val recordingDao: RecordingDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupRecording(recordingId: String): RecordingScaffoldModel {
        val recording = recordingDao.getRecording(recordingId)
        val artistCreditNames = artistCreditDao.getArtistCreditNamesForEntity(recordingId)
        val urlRelations = relationRepository.getEntityUrlRelationships(recordingId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(recordingId)
        if (recording != null &&
            artistCreditNames.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            return recording.toRecordingScaffoldModel(
                artistCreditNames = artistCreditNames,
                urls = urlRelations,
            )
        }

        val recordingMusicBrainzModel = musicBrainzApi.lookupRecording(recordingId)
        cache(recordingMusicBrainzModel)
        return lookupRecording(recordingId)
    }

    private fun cache(recording: RecordingMusicBrainzModel) {
        recordingDao.withTransaction {
            recordingDao.insert(recording)
            relationRepository.insertAllUrlRelations(
                entityId = recording.id,
                relationMusicBrainzModels = recording.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupRecording(
            recordingId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
