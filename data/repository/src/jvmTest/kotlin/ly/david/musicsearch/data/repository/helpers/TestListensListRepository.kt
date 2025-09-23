package ly.david.musicsearch.data.repository.helpers

import kotlinx.coroutines.test.TestScope
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.data.listenbrainz.api.ListensResponse
import ly.david.musicsearch.data.listenbrainz.api.ManualMappingResponse
import ly.david.musicsearch.data.listenbrainz.api.RecordingMetadata
import ly.david.musicsearch.data.listenbrainz.api.TokenValidationResponse
import ly.david.musicsearch.data.repository.listen.ListensListRepositoryImpl
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListensListRepository

interface TestListensListRepository {

    val listenDao: ListenDao
    val coroutineDispatchers: CoroutineDispatchers

    fun createListensListRepository(
        response: ListensResponse,
    ): ListensListRepository {
        return ListensListRepositoryImpl(
            listenDao = listenDao,
            listenBrainzApi = object : ListenBrainzApi {
                override suspend fun validateToken(token: String): TokenValidationResponse {
                    return TokenValidationResponse(valid = false)
                }

                override suspend fun getListensByUser(
                    username: String,
                    minTimestamp: Long?,
                    maxTimestamp: Long?,
                ): ListensResponse {
                    return response
                }

                override suspend fun submitManualMapping(
                    recordingMessyBrainzId: String,
                    recordingMusicBrainzId: String,
                ) {
                    // no-op
                }

                override suspend fun getManualMapping(recordingMessyBrainzId: String): ManualMappingResponse {
                    return ManualMappingResponse(
                        mapping = ManualMappingResponse.Mapping(
                            recordingMsid = recordingMessyBrainzId,
                            recordingMbid = "",
                        ),
                    )
                }

                override suspend fun getRecordingMetadata(recordingMusicBrainzId: String): RecordingMetadata? {
                    return null
                }
            },
            coroutineScope = TestScope(coroutineDispatchers.io),
        )
    }
}
