package ly.david.musicsearch.data.listenbrainz.auth

import kotlinx.coroutines.withContext
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzLoginState
import ly.david.musicsearch.shared.domain.listen.UpdateListenBrainzToken

class UpdateListenBrainzTokenImpl(
    private val authStore: ListenBrainzAuthStore,
    private val listenBrainzApi: ListenBrainzApi,
    private val coroutineDispatchers: CoroutineDispatchers,
) : UpdateListenBrainzToken {
    override suspend operator fun invoke(token: String): ListenBrainzLoginState? {
        return when {
            token.isBlank() && authStore.getUserToken().isNotEmpty() -> {
                authStore.setUserToken("")
                authStore.setUsername("")
                ListenBrainzLoginState.LoggedOut
            }

            token.isBlank() -> {
                null
            }

            else -> try {
                withContext(coroutineDispatchers.io) {
                    val response = listenBrainzApi.validateToken(token = token)
                    if (response.valid) {
                        authStore.setUserToken(token)
                        authStore.setUsername(response.userName.orEmpty())
                        ListenBrainzLoginState.LoggedIn
                    } else {
                        ListenBrainzLoginState.InvalidToken
                    }
                }
            } catch (ex: HandledException) {
                ListenBrainzLoginState.OtherError(ex.message ?: "Unknown error")
            }
        }
    }
}
