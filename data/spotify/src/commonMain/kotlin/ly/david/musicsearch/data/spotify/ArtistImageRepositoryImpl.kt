package ly.david.musicsearch.data.spotify

import io.ktor.client.plugins.ClientRequestException
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.image.ImageUrlDao
import ly.david.musicsearch.core.models.image.ImageUrls
import ly.david.musicsearch.data.spotify.api.SpotifyApi
import ly.david.musicsearch.data.spotify.api.getLargeImageUrl
import ly.david.musicsearch.data.spotify.api.getThumbnailImageUrl
import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository

/**
 * Logic to retrieve release cover art path.
 */
class ArtistImageRepositoryImpl(
    private val spotifyApi: SpotifyApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ArtistImageRepository {

    /**
     * Returns a url to the artist image.
     * Empty if none found.
     *
     * Also saves it to db.
     */
    override suspend fun getArtistImageFromNetwork(
        artistMbid: String,
        spotifyUrl: String,
    ): String {
        return try {
            val spotifyArtistId = spotifyUrl.split("/").last()

            val spotifyArtist = spotifyApi.getArtist(spotifyArtistId)
            val thumbnailUrl = spotifyArtist.getThumbnailImageUrl()
            val largeUrl = spotifyArtist.getLargeImageUrl()
            imageUrlDao.saveUrls(
                mbid = artistMbid,
                imageUrls = listOf(
                    ImageUrls(
                        thumbnailUrl = thumbnailUrl,
                        largeUrl = largeUrl,
                    ),
                ),
            )
            largeUrl
        } catch (ex: ClientRequestException) {
            logger.e(ex)
            ""
        }
    }

    override fun deleteImage(
        artistMbid: String,
    ) {
        imageUrlDao.deleteAllUrlsById(artistMbid)
    }
}
