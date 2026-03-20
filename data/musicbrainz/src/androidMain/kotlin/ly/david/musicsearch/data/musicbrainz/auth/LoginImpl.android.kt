package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.shared.domain.auth.Login
import net.openid.appauth.TokenRequest

internal class LoginImpl(
    private val getAndSaveToken: GetAndSaveToken,
) : Login {
    override suspend operator fun invoke(tokenRequestJsonString: String): Boolean {
        val tokenRequest: TokenRequest = TokenRequest.jsonDeserialize(tokenRequestJsonString)
        return getAndSaveToken(
            authorizationCode = tokenRequest.authorizationCode.orEmpty(),
            codeVerifier = tokenRequest.codeVerifier,
        )
    }
}
