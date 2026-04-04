package ly.david.musicsearch.shared.feature.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithEntity
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.test.image.InitializeFakeImageLoader
import ly.david.musicsearch.ui.common.preview.PreviewManyDevices
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

private val images = listOf(
    ImageMetadataWithEntity(
        imageMetadata = ImageMetadata.InternetArchive(
            imageId = ImageId(1),
            rawThumbnailUrl = "www.example.com/blue",
            rawLargeUrl = "www.example.com/blue",
            types = persistentListOf("Front"),
            comment = "page 1",
        ),
        musicBrainzEntity = MusicBrainzEntity(
            id = "r",
            type = MusicBrainzEntityType.RELEASE,
        ),
        name = "Title",
        disambiguation = "with disambiguation",
    ),
    ImageMetadataWithEntity(
        imageMetadata = ImageMetadata.Spotify(
            imageId = ImageId(2),
            rawThumbnailUrl = "www.example.com/red",
            rawLargeUrl = "www.example.com/red",
        ),
    ),
)
private val imagesFlow = MutableStateFlow(
    PagingData.from(images),
)

@PreviewLightDark
@Composable
internal fun PreviewImagesGridUi() {
    InitializeFakeImageLoader()
    PreviewWithTransitionAndOverlays {
        ImagesUi(
            state = ImagesUiState(
                title = ImagesTitle.All,
                imageMetadataPagingDataFlow = imagesFlow,
            ),
            isCompact = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewImagesPagerUiCompact() {
    InitializeFakeImageLoader()
    PreviewWithTransitionAndOverlays {
        ImagesUi(
            state = ImagesUiState(
                title = ImagesTitle.Selected(
                    typeAndComment = "Front",
                    page = 1,
                    totalPages = 2,
                ),
                subtitle = "Title (with disambiguation)",
                imageMetadataPagingDataFlow = imagesFlow,
                selectedImageIndex = 0,
                selectedImageMetadata = images[0],
            ),
            isCompact = true,
        )
    }
}

@PreviewManyDevices
@Composable
internal fun PreviewImagesPagerUiNonCompact() {
    InitializeFakeImageLoader()
    PreviewWithTransitionAndOverlays {
        ImagesUi(
            state = ImagesUiState(
                title = ImagesTitle.Selected(
                    typeAndComment = "Front",
                    page = 1,
                    totalPages = 2,
                ),
                subtitle = "Title (with disambiguation)",
                imageMetadataPagingDataFlow = imagesFlow,
                selectedImageIndex = 0,
                selectedImageMetadata = images[0],
            ),
            isCompact = false,
        )
    }
}
