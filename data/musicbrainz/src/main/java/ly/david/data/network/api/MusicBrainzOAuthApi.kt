package ly.david.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters

interface MusicBrainzAuthApi {

    suspend fun getUserInfo(): UserInfo

    suspend fun logout(
        token: String,
        clientId: String,
        clientSecret: String,
    )
}

interface MusicBrainzAuthApiImpl : MusicBrainzAuthApi {
    val httpClient: HttpClient

    override suspend fun getUserInfo(): UserInfo {
        return httpClient.get("$MUSIC_BRAINZ_BASE_URL/oauth2/$USER_INFO").body()
    }

    override suspend fun logout(
        token: String,
        clientId: String,
        clientSecret: String,
    ) {
        httpClient.submitForm(
            url = "$MUSIC_BRAINZ_BASE_URL/oauth2/revoke",
            formParameters = parameters {
                append("token", token)
                append("client_id", clientId)
                append("client_secret", clientSecret)
            },
        )
    }
}
