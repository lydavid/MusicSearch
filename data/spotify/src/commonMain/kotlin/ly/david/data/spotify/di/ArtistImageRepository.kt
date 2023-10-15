package ly.david.data.spotify.di

import io.ktor.client.plugins.ClientRequestException
import ly.david.musicsearch.core.models.image.ImageUrlDao
import ly.david.musicsearch.core.models.logging.Logger
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.getLargeImageUrl
import ly.david.data.spotify.api.getThumbnailImageUrl
import org.koin.core.annotation.Single

/**
 * Logic to retrieve release cover art path.
 */
@Single
class ArtistImageRepository(
    private val spotifyApi: SpotifyApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
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
            imageUrlDao.saveUrl(
                mbid = artistMbid,
                thumbnailUrl = thumbnailUrl,
                largeUrl = largeUrl,
            )
            largeUrl
        } catch (ex: ClientRequestException) {
            logger.e(ex)
            ""
        }
    }
}
