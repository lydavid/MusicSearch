package ly.david.musicsearch.shared.domain.image

import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface ImageMetadataRepository {
    /**
     * Returns metadata for an image, including its url. An image that does not exist will have an empty url.
     *
     * Also saves it to db.
     *
     * Appropriate for getting a single image in a details view.
     */
    suspend fun getAndSaveImageMetadata(
        detailsModel: MusicBrainzDetailsModel,
        entity: MusicBrainzEntity,
        forceRefresh: Boolean,
    ): ImageMetadataWithCount
}

class ImageMetadataRepositoryImpl(
    private val artistImageRepository: ArtistImageRepository,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
) : ImageMetadataRepository {
    override suspend fun getAndSaveImageMetadata(
        detailsModel: MusicBrainzDetailsModel,
        entity: MusicBrainzEntity,
        forceRefresh: Boolean,
    ): ImageMetadataWithCount {
        return when (entity) {
            MusicBrainzEntity.ARTIST -> {
                ImageMetadataWithCount(
                    imageMetadata = artistImageRepository.getArtistImageMetadata(
                        detailsModel = detailsModel,
                        forceRefresh = forceRefresh,
                    ),
                    count = 1,
                )
            }

            else -> {
                musicBrainzImageMetadataRepository.getAndSaveImageMetadata(
                    mbid = detailsModel.id,
                    entity = entity,
                    forceRefresh = forceRefresh,
                )
            }
        }
    }
}
