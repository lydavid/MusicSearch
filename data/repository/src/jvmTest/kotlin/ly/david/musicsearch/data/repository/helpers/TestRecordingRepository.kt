package ly.david.musicsearch.data.repository.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.recording.RecordingRepositoryImpl
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.recording.RecordingRepository

interface TestRecordingRepository {
    val relationsMetadataDao: RelationsMetadataDao
    val detailsMetadataDao: DetailsMetadataDao
    val relationDao: RelationDao
    val recordingDao: RecordingDao
    val artistCreditDao: ArtistCreditDao
    val aliasDao: AliasDao

    fun createRecordingRepository(
        musicBrainzModel: RecordingMusicBrainzNetworkModel,
        fakeBrowseUsername: String = "",
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
            aliasDao = aliasDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupRecording(
                    recordingId: String,
                    include: String,
                ): RecordingMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            listenBrainzAuthStore = object : ListenBrainzAuthStore {
                override suspend fun getUserToken(): String {
                    return ""
                }

                override fun observeUserToken(): Flow<String> {
                    return flowOf("")
                }

                override suspend fun setUserToken(token: String) {
                    // No-op
                }

                override val username: Flow<String>
                    get() = flowOf("")

                override suspend fun setUsername(username: String) {
                    // No-op
                }

                override val browseUsername: Flow<String>
                    get() = flowOf(fakeBrowseUsername)

                override suspend fun setBrowseUsername(username: String) {
                    // No-op
                }
            },
            listenBrainzRepository = object : ListenBrainzRepository {
                override fun getBaseUrl(): String {
                    return ""
                }
            },
        )
    }
}
