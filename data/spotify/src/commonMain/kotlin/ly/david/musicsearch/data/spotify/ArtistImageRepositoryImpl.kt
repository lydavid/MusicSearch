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
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import kotlin.coroutines.cancellation.CancellationException

class ArtistImageRepositoryImpl(
    private val spotifyApi: SpotifyApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ArtistImageRepository {

    override suspend fun getArtistImageMetadata(
        artistDetailsModel: ArtistDetailsModel,
        forceRefresh: Boolean,
    ): ImageMetadata {
        if (forceRefresh) {
            imageUrlDao.deleteAllImageMetadtaById(artistDetailsModel.id)
        }

        val cachedImageUrl = imageUrlDao.getFrontImageMetadata(artistDetailsModel.id)
        return if (cachedImageUrl == null) {
            saveArtistImageMetadataFromNetwork(artistDetailsModel)
            imageUrlDao.getFrontImageMetadata(artistDetailsModel.id) ?: ImageMetadata()
        } else {
            cachedImageUrl
        }
    }

    private suspend fun saveArtistImageMetadataFromNetwork(
        artistDetailsModel: ArtistDetailsModel,
    ) {
        try {
            val spotifyUrl =
                artistDetailsModel.urls.firstOrNull { it.name.contains("open.spotify.com/artist/") }?.name

            val imageMetadata: ImageMetadata = if (spotifyUrl == null) {
                ImageMetadata()
            } else {
                val spotifyArtistId = spotifyUrl.split("/").last()
                val spotifyArtist: SpotifyArtist = spotifyApi.getArtist(spotifyArtistId)
                ImageMetadata(
                    thumbnailUrl = spotifyArtist.getThumbnailImageUrl(),
                    largeUrl = spotifyArtist.getLargeImageUrl(),
                )
            }

            imageUrlDao.saveImageMetadata(
                mbid = artistDetailsModel.id,
                imageMetadataList = listOf(imageMetadata),
            )
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: HandledException) {
            if (ex.errorResolution == ErrorResolution.None) {
                imageUrlDao.saveImageMetadata(
                    mbid = artistDetailsModel.id,
                    imageMetadataList = listOf(ImageMetadata()),
                )
            } else {
                logger.e(ex)
            }
        } catch (ex: Exception) {
            logger.e(ex)
        }
    }
}
