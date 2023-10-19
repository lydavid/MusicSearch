package ly.david.musicsearch.data.repository.recording

import ly.david.musicsearch.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.recording.RecordingScaffoldModel
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.recording.RecordingRepository
import ly.david.musicsearch.domain.relation.RelationRepository

class RecordingRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val recordingDao: RecordingDao,
    private val artistCreditDao: ArtistCreditDao,
    private val relationRepository: RelationRepository,
) : RecordingRepository {

    override suspend fun lookupRecording(recordingId: String): RecordingScaffoldModel {
        val recording = recordingDao.getRecordingForDetails(recordingId)
        val artistCredits = artistCreditDao.getArtistCreditsForEntity(recordingId)
        val urlRelations = relationRepository.getEntityUrlRelationships(recordingId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(recordingId)
        if (recording != null &&
            artistCredits.isNotEmpty() &&
            hasUrlsBeenSavedForEntity
        ) {
            return recording.copy(
                artistCredits = artistCredits,
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

            val relationWithOrderList = recording.relations.toRelationWithOrderList(recording.id)
            relationRepository.insertAllUrlRelations(
                entityId = recording.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
