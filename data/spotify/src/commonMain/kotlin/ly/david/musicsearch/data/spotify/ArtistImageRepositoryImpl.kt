package ly.david.musicsearch.data.spotify

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.spotify.api.SpotifyApi
import ly.david.musicsearch.data.spotify.api.SpotifyArtist
import ly.david.musicsearch.data.spotify.api.getLargeImageUrl
import ly.david.musicsearch.data.spotify.api.getThumbnailImageUrl
import ly.david.musicsearch.data.spotify.auth.SpotifyOAuthInfo
import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository
import ly.david.musicsearch.shared.domain.common.trimProtocolAndExtension
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithCount
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import kotlin.coroutines.cancellation.CancellationException

class ArtistImageRepositoryImpl(
    private val spotifyOAuthInfo: SpotifyOAuthInfo,
    private val spotifyApi: SpotifyApi,
    private val wikimediaRepository: WikimediaRepository,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ArtistImageRepository {

    override suspend fun getArtistImageMetadata(
        detailsModel: MusicBrainzDetailsModel,
        forceRefresh: Boolean,
    ): ImageMetadataWithCount {
        if (forceRefresh) {
            imageUrlDao.deleteAllImageMetadtaById(detailsModel.id)
        }

        val cachedImageUrl = imageUrlDao.getFrontImageMetadata(detailsModel.id)
        return if (cachedImageUrl == null) {
            saveArtistImageMetadataFromNetwork(detailsModel)
            imageUrlDao.getFrontImageMetadata(detailsModel.id) ?: ImageMetadataWithCount()
        } else {
            cachedImageUrl
        }
    }

    private suspend fun saveArtistImageMetadataFromNetwork(
        detailsModel: MusicBrainzDetailsModel,
    ) {
        try {
            val noSpotifySecrets = spotifyOAuthInfo.clientSecret.isEmpty() || spotifyOAuthInfo.clientId.isEmpty()
            val spotifyUrl =
                detailsModel.urls.firstOrNull { it.name.contains("open.spotify.com/artist/") }?.name

            val imageMetadata = if (noSpotifySecrets || spotifyUrl == null) {
                wikimediaRepository.getWikimediaImage(
                    urls = detailsModel.urls,
                )
            } else {
                val spotifyArtistId = spotifyUrl.split("/").last()
                val spotifyArtist: SpotifyArtist = spotifyApi.getArtist(spotifyArtistId)
                ImageMetadata(
                    thumbnailUrl = spotifyArtist.getThumbnailImageUrl().trimProtocolAndExtension(),
                    largeUrl = spotifyArtist.getLargeImageUrl().trimProtocolAndExtension(),
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
