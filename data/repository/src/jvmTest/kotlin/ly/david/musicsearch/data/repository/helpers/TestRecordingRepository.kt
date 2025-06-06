package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.recording.RecordingRepositoryImpl
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.recording.RecordingRepository

interface TestRecordingRepository {
    val relationsMetadataDao: RelationsMetadataDao
    val detailsMetadataDao: DetailsMetadataDao
    val relationDao: RelationDao
    val recordingDao: RecordingDao
    val artistCreditDao: ArtistCreditDao

    fun createRecordingRepository(
        musicBrainzModel: RecordingMusicBrainzNetworkModel,
    ): RecordingRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupRecording(
                    recordingId: String,
                    include: String,
                ): RecordingMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
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
                ): RecordingMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
        )
    }
}
