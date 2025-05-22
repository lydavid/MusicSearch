package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeBrowseApi
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.repository.recording.RecordingsListRepositoryImpl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.recording.RecordingsListRepository

interface TestRecordingsListRepository {
    val recordingDao: RecordingDao
    val collectionEntityDao: CollectionEntityDao
    val browseRemoteMetadataDao: BrowseRemoteMetadataDao

    fun createRecordingsListRepository(
        recordings: List<RecordingMusicBrainzModel>,
    ): RecordingsListRepository {
        return RecordingsListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            recordingDao = recordingDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseRecordingsByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseRecordingsResponse {
                    return BrowseRecordingsResponse(
                        count = recordings.size,
                        offset = 0,
                        musicBrainzModels = recordings,
                    )
                }
            },
        )
    }
}
