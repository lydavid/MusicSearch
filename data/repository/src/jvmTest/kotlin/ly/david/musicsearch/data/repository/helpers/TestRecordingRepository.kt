package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.recording.RecordingRepositoryImpl
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.recording.RecordingRepository

interface TestRecordingRepository {
    val entityHasRelationsDao: EntityHasRelationsDao
    val visitedDao: VisitedDao
    val relationDao: RelationDao
    val recordingDao: RecordingDao
    val artistCreditDao: ArtistCreditDao

    fun createRecordingRepository(
        musicBrainzModel: RecordingMusicBrainzModel,
    ): RecordingRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupRecording(
                    recordingId: String,
                    include: String,
                ): RecordingMusicBrainzModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            visitedDao = visitedDao,
            relationDao = relationDao,
        )
        return RecordingRepositoryImpl(
            recordingDao = recordingDao,
            relationRepository = relationRepository,
            artistCreditDao = artistCreditDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupRecording(
                    recordingId: String,
                    include: String,
                ): RecordingMusicBrainzModel {
                    return musicBrainzModel
                }
            },
        )
    }
}
