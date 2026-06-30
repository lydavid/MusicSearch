package ly.david.musicsearch.data.spotify.auth.store

import kotlinx.coroutines.flow.first
import ly.david.musicsearch.shared.domain.image.ArtistImageSource
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.spotify.SpotifyOAuthInfo

interface SpotifyAuthRepository {
    suspend fun getOAuthInfo(): SpotifyOAuthInfo
}

class SpotifyAuthRepositoryImpl(
    private val defaultSpotifyOAuthInfo: SpotifyOAuthInfo,
    private val appPreferences: AppPreferences,
) : SpotifyAuthRepository {

    override suspend fun getOAuthInfo(): SpotifyOAuthInfo {
        return when (val source = appPreferences.artistImageSource.first()) {
            ArtistImageSource.Wikimedia -> SpotifyOAuthInfo()

            ArtistImageSource.Spotify.Default -> {
                if (defaultSpotifyOAuthInfo.clientId.isEmpty()) {
                    SpotifyOAuthInfo()
                } else {
                    defaultSpotifyOAuthInfo
                }
            }

            is ArtistImageSource.Spotify.Custom -> {
                if (source.clientId.isEmpty()) {
                    SpotifyOAuthInfo()
                } else {
                    SpotifyOAuthInfo(
                        clientId = source.clientId,
                        clientSecret = source.clientSecret,
                    )
                }
            }
        }
    }
}
