package ly.david.musicsearch.shared.domain.auth

import kotlinx.coroutines.flow.Flow

interface MusicBrainzAuthStore {
    suspend fun getAccessToken(): String?
    val accessToken: Flow<String?>

    suspend fun getRefreshToken(): String?

    suspend fun getExpiryTimeInEpochSeconds(): Long?

    suspend fun userHasAllAuthScopes(): Boolean

    /**
     * We store [scope] to determine which scopes the user has authenticated with.
     * When we add new scope, we can compare the stored scope with [MUSIC_BRAINZ_OAUTH_SCOPE].
     * When refreshing tokens, do not update [scope]. Pass `null` to skip updating it.
     * To remove its value, pass empty string.
     *
     * To remove the values of [accessToken] and [refreshToken], pass an empty string.
     */
    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
        scope: String? = null,
    )

    val username: Flow<String>
    suspend fun setUsername(username: String)
}
