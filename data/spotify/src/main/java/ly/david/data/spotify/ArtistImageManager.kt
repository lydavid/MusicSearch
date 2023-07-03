package ly.david.data.spotify

import java.net.HttpURLConnection.HTTP_NOT_FOUND
import ly.david.data.image.ImageUrlSaver
import retrofit2.HttpException

/**
 * Logic to retrieve release cover art path.
 */
interface ArtistImageManager {

    val spotifyApi: SpotifyApi
    val imageUrlSaver: ImageUrlSaver

    /**
     * Returns a url to the artist image.
     * Empty if none found.
     *
     * Also saves it to db.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getArtistImageFromNetwork(artistMbid: String, spotifyUrl: String): String {
        return try {
            val spotifyArtistId = spotifyUrl.split("/").last()

            val url = spotifyApi.getArtist(spotifyArtistId = spotifyArtistId).getLargeImageUrl()
            imageUrlSaver.saveUrl(artistMbid, url)
            return url
        } catch (ex: HttpException) {
            if (ex.code() == HTTP_NOT_FOUND) {
                imageUrlSaver.saveUrl(artistMbid, "")
            }
            ""
        }
    }
}
