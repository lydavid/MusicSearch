package ly.david.musicsearch.data.musicbrainz.auth.api

import io.ktor.client.HttpClient

interface MusicBrainzOAuthApi {

    companion object {
        fun create(
            httpClient: HttpClient,
        ): MusicBrainzOAuthApi {
            return MusicBrainzOAuthApiImpl(
                httpClient = httpClient,
            )
        }
    }

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
        refreshToken: String,
    ): MusicBrainzOAuthResponse
}
