package ly.david.musicsearch.shared.domain.spotify

import kotlinx.coroutines.flow.Flow

interface IsUserSpotifyAuthSet {
    operator fun invoke(): Flow<Boolean>
}
