package ly.david.data.network.api

import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.SpotifyArtist
import ly.david.data.spotify.api.SpotifyImage

class FakeSpotifyApi : SpotifyApi {
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
