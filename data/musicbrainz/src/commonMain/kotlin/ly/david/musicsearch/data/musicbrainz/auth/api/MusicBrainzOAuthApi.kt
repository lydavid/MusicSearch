package ly.david.musicsearch.data.musicbrainz.auth.api

import io.ktor.client.HttpClient
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo

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
        musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
        grantType: String,
        refreshToken: String,
    ): AccessToken
}
