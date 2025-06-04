package ly.david.musicsearch.data.spotify

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.spotify.api.SpotifyApi
import ly.david.musicsearch.data.spotify.api.SpotifyArtist
import ly.david.musicsearch.data.spotify.api.getLargeImageUrl
import ly.david.musicsearch.data.spotify.api.getThumbnailImageUrl
import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithCount
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import kotlin.coroutines.cancellation.CancellationException

class ArtistImageRepositoryImpl(
    private val spotifyApi: SpotifyApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ArtistImageRepository {

    override suspend fun getArtistImageMetadata(
        detailsModel: MusicBrainzDetailsModel,
        forceRefresh: Boolean,
    ): ImageMetadata {
        if (forceRefresh) {
            imageUrlDao.deleteAllImageMetadtaById(detailsModel.id)
        }

        val cachedImageUrl = imageUrlDao.getFrontImageMetadata(detailsModel.id)
        return if (cachedImageUrl == null) {
            saveArtistImageMetadataFromNetwork(detailsModel)
            imageUrlDao.getFrontImageMetadata(detailsModel.id) ?: ImageMetadataWithCount()
        } else {
            cachedImageUrl
        }.imageMetadata
    }

    private suspend fun saveArtistImageMetadataFromNetwork(
        detailsModel: MusicBrainzDetailsModel,
    ) {
        try {
            val spotifyUrl =
                detailsModel.urls.firstOrNull { it.name.contains("open.spotify.com/artist/") }?.name

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
                mbid = detailsModel.id,
                imageMetadataList = listOf(imageMetadata),
            )
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: HandledException) {
            if (ex.errorResolution == ErrorResolution.None) {
                imageUrlDao.saveImageMetadata(
                    mbid = detailsModel.id,
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
