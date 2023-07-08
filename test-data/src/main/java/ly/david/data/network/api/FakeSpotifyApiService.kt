package ly.david.data.network.api

import ly.david.data.spotify.SpotifyApi
import ly.david.data.spotify.SpotifyArtist
import ly.david.data.spotify.SpotifyImage

class FakeSpotifyApiService : SpotifyApi {
    override suspend fun getArtist(spotifyArtistId: String): SpotifyArtist {
        return SpotifyArtist(
            images = listOf(
                SpotifyImage(
                    "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
                    height = 300,
                    width = 300
                )
            )
        )
    }
}
