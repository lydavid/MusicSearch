package ly.david.musicsearch.data.musicbrainz.auth

data class MusicBrainzOAuthInfo(
    val clientId: String,
    val clientSecret: String,
    val authorizationEndpoint: String,
    val tokenEndpoint: String,
    val endSessionEndpoint: String,
    val scope: String,
)
