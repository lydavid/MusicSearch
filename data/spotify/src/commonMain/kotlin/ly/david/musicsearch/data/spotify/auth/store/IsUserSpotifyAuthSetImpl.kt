package ly.david.musicsearch.data.spotify.auth.store

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.spotify.IsUserSpotifyAuthSet

class IsUserSpotifyAuthSetImpl(
    private val spotifyAuthStore: SpotifyAuthStore,
) : IsUserSpotifyAuthSet {
    override fun invoke(): Flow<Boolean> {
        return spotifyAuthStore.observeUserSpotifyAuthSet()
    }
}
