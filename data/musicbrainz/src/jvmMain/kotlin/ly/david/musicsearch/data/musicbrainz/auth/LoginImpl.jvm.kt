package ly.david.musicsearch.data.musicbrainz.auth

import kotlinx.coroutines.withContext
import ly.david.musicsearch.shared.domain.auth.Login
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers

internal class LoginImpl(
    private val getAndSaveToken: GetAndSaveToken,
    private val dispatchers: CoroutineDispatchers,
) : Login {
    override suspend operator fun invoke(authCode: String): Boolean {
        return withContext(dispatchers.io) {
            getAndSaveToken(
                authorizationCode = authCode,
            )
        }
    }
}
