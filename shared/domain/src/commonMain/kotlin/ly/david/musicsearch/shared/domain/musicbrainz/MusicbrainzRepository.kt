package ly.david.musicsearch.shared.domain.musicbrainz

interface MusicbrainzRepository {
    fun getBaseUrl(): String
    fun getOAuthBaseUrl(): String
    fun getAuthorizationEndpoint(): String
    fun getTokenEndpoint(): String
    fun getRevokeEndpoint(): String
}
