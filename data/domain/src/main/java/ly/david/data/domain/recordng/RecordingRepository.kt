package ly.david.data.domain.recordng

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RecordingDao
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
        recordingDao.insert(recordingMusicBrainzModel)
        artistCreditDao.insertArtistCredits(
            entityId = recordingId,
            artistCredits = recordingMusicBrainzModel.artistCredits,
        )
        relationRepository.insertAllUrlRelations(
            entityId = recordingId,
            relationMusicBrainzModels = recordingMusicBrainzModel.relations,
        )
        return lookupRecording(recordingId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupRecording(
            recordingId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
