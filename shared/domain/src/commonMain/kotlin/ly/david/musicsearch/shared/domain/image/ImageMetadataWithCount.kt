package ly.david.musicsearch.shared.domain.image

data class ImageMetadataWithCount(
    val imageMetadata: ImageMetadata = ImageMetadata(),
    val count: Int = 0,
)
