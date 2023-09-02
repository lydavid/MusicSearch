package ly.david.data.spotify

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.core.image.ImageUrlSaver
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.getLargeImageUrl
import ly.david.data.spotify.api.getThumbnailImageUrl

/**
 * Logic to retrieve release cover art path.
 */
@Singleton
class ArtistImageRepository @Inject constructor(
    private val spotifyApi: SpotifyApi,
    private val imageUrlSaver: ImageUrlSaver,
) {

    /**
     * Returns a url to the artist image.
     * Empty if none found.
     *
     * Also saves it to db.
     */
    suspend fun getArtistImageFromNetwork(artistMbid: String, spotifyUrl: String): String {
        return try {
            val spotifyArtistId = spotifyUrl.split("/").last()

            val spotifyArtist = spotifyApi.getArtist(spotifyArtistId)
            val thumbnailUrl = spotifyArtist.getThumbnailImageUrl()
            val largeUrl = spotifyArtist.getLargeImageUrl()
            imageUrlSaver.saveUrl(
                mbid = artistMbid,
                thumbnailUrl = thumbnailUrl,
                largeUrl = largeUrl
            )
            largeUrl
        } catch (ex: ClientRequestException) {
            // Just log, don't offer retry for these, cause it won't be successful
            ""
        } catch (ex: ServerResponseException) {
            // TODO: should offer retry
            ""
        }
    }
}
