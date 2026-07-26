package ly.david.musicsearch.data.musicbrainz.auth.api

import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo

interface MusicBrainzOAuthApi {
    suspend fun getAccessToken(
        musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
        grantType: String,
        refreshToken: String,
    ): AccessToken
}
