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
    ): ImageUrls {
        if (forceRefresh) {
            imageUrlDao.deleteAllUrlsById(artistDetailsModel.id)
        }

        val cachedImageUrl = imageUrlDao.getFrontCoverUrl(artistDetailsModel.id)
        return if (cachedImageUrl == null) {
            saveArtistImageUrlFromNetwork(artistDetailsModel)
            imageUrlDao.getFrontCoverUrl(artistDetailsModel.id) ?: ImageUrls()
        } else {
            cachedImageUrl
        }
    }

    private suspend fun saveArtistImageUrlFromNetwork(
        artistDetailsModel: ArtistDetailsModel,
    ) {
        try {
            val spotifyUrl =
                artistDetailsModel.urls.firstOrNull { it.name.contains("open.spotify.com/artist/") }?.name

            val imageUrl: ImageUrls = if (spotifyUrl == null) {
                ImageUrls()
            } else {
                val spotifyArtistId = spotifyUrl.split("/").last()
                val spotifyArtist: SpotifyArtist = spotifyApi.getArtist(spotifyArtistId)
                ImageUrls(
                    thumbnailUrl = spotifyArtist.getThumbnailImageUrl(),
                    largeUrl = spotifyArtist.getLargeImageUrl(),
                )
            }

            imageUrlDao.saveUrls(
                mbid = artistDetailsModel.id,
                imageUrls = listOf(imageUrl),
            )
        } catch (ex: HandledException) {
            if (ex.errorResolution == ErrorResolution.None) {
                imageUrlDao.saveUrls(
                    mbid = artistDetailsModel.id,
                    imageUrls = listOf(ImageUrls()),
                )
            } else {
                logger.e(ex)
            }
        } catch (ex: Exception) {
            logger.e(ex)
        }
    }
}
