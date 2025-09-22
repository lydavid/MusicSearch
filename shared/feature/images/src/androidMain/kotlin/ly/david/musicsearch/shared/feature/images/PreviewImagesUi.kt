package ly.david.musicsearch.shared.feature.images

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.test.image.InitializeFakeImageLoader
import ly.david.musicsearch.ui.common.preview.PreviewManyDevices
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

val images = MutableStateFlow(
    PagingData.from(
        listOf(
            ImageMetadata(
                imageId = ImageId(1),
                thumbnailUrl = "www.example.com/blue",
                largeUrl = "www.example.com/blue",
                types = persistentListOf("Front"),
                comment = "",
            ),
            ImageMetadata(
                imageId = ImageId(2),
                thumbnailUrl = "www.example.com/red",
                largeUrl = "www.example.com/red",
                types = persistentListOf("Back"),
                comment = "",
            ),
        ),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewImagesGridUi() {
    InitializeFakeImageLoader()
    PreviewWithSharedElementTransition {
        ImagesUi(
            state = ImagesUiState(
                title = ImagesTitle.All,
                imageMetadataPagingDataFlow = images,
            ),
            isCompact = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewImagesPagerUiCompact() {
    InitializeFakeImageLoader()
    PreviewWithSharedElementTransition {
        ImagesUi(
            state = ImagesUiState(
                title = ImagesTitle.Selected(
                    typeAndComment = "Front",
                    page = 1,
                    totalPages = 2,
                ),
                subtitle = "Title (with disambiguation)",
                imageMetadataPagingDataFlow = images,
                selectedImageIndex = 0,
            ),
            isCompact = true,
        )
    }
}

@PreviewManyDevices
@Composable
internal fun PreviewImagesPagerUiNonCompact() {
    InitializeFakeImageLoader()
    PreviewWithSharedElementTransition {
        ImagesUi(
            state = ImagesUiState(
                title = ImagesTitle.Selected(
                    typeAndComment = "Front",
                    page = 1,
                    totalPages = 2,
                ),
                subtitle = "Title (with disambiguation)",
                imageMetadataPagingDataFlow = images,
                selectedImageIndex = 0,
            ),
            isCompact = false,
        )
    }
}
