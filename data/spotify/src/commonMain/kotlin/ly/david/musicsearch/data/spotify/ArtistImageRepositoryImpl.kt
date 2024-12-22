package ly.david.musicsearch.data.spotify

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.spotify.api.SpotifyApi
import ly.david.musicsearch.data.spotify.api.SpotifyArtist
import ly.david.musicsearch.data.spotify.api.getLargeImageUrl
import ly.david.musicsearch.data.spotify.api.getThumbnailImageUrl
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImageUrls

class ArtistImageRepositoryImpl(
    private val spotifyApi: SpotifyApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ArtistImageRepository {

    override suspend fun getArtistImageUrl(
        artistDetailsModel: ArtistDetailsModel,
        forceRefresh: Boolean,
    ): String {
        if (forceRefresh) {
            imageUrlDao.deleteAllUrlsById(artistDetailsModel.id)
        }

        val cachedImageUrls = imageUrlDao.getAllUrlsById(artistDetailsModel.id)
        return if (cachedImageUrls.isNotEmpty()) {
            return cachedImageUrls.first().largeUrl
        } else {
            getArtistImageUrlFromNetwork(artistDetailsModel)
        }
    }

    private suspend fun getArtistImageUrlFromNetwork(
        artistDetailsModel: ArtistDetailsModel,
    ): String {
        return try {
            val spotifyUrl =
                artistDetailsModel.urls.firstOrNull { it.name.contains("open.spotify.com/artist/") }?.name
                    ?: return ""
            val spotifyArtistId = spotifyUrl.split("/").last()

            val spotifyArtist: SpotifyArtist = spotifyApi.getArtist(spotifyArtistId)
            val largeUrl = spotifyArtist.getLargeImageUrl()
            cache(
                artistDetailsModel = artistDetailsModel,
                spotifyArtist = spotifyArtist,
            )
            largeUrl
        } catch (ex: HandledException) {
            // TODO: if 400, prompt user to input their own Spotify client secret if they want to load artist images.
            //  UI should allow dismissing if they don't want to.
            //  Then don't reprompt. But allow inputting client secret from settings.
            if (ex.errorResolution != ErrorResolution.None) {
                logger.e(ex)
            }
            ""
        } catch (ex: Exception) {
            logger.e(ex)
            ""
        }
    }

    private fun cache(
        artistDetailsModel: ArtistDetailsModel,
        spotifyArtist: SpotifyArtist,
    ) {
        imageUrlDao.saveUrls(
            mbid = artistDetailsModel.id,
            imageUrls = listOf(
                ImageUrls(
                    thumbnailUrl = spotifyArtist.getThumbnailImageUrl(),
                    largeUrl = spotifyArtist.getLargeImageUrl(),
                ),
            ),
        )
    }
}
