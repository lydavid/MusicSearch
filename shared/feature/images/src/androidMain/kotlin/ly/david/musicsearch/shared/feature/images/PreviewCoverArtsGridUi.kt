package ly.david.musicsearch.shared.feature.images

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.test.image.InitializeFakeImageLoader
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

val images = MutableStateFlow(
    PagingData.from(
        listOf(
            ImageMetadata(
                databaseId = 1,
                thumbnailUrl = "https://www.example.com/blue.jpg",
                largeUrl = "https://www.example.com/blue.jpg",
                types = persistentListOf("Front"),
                comment = "",
            ),
            ImageMetadata(
                databaseId = 2,
                thumbnailUrl = "https://www.example.com/red.jpg",
                largeUrl = "https://www.example.com/red.jpg",
                types = persistentListOf("Back"),
                comment = "",
            ),
        ),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewCoverArtsGridUi() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsGridUi(
                state = CoverArtsUiState(
                    title = "Cover arts",
                    imageMetadataList = images.collectAsLazyPagingItems(),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCoverArtsPagerUiCompact() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsGridUi(
                state = CoverArtsUiState(
                    title = "Front",
                    subtitle = "1/2",
                    imageMetadataList = images.collectAsLazyPagingItems(),
                    selectedImageIndex = 0,
                ),
                isCompact = true,
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewCoverArtsPagerUiNonCompact() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsGridUi(
                state = CoverArtsUiState(
                    title = "Front",
                    subtitle = "1/2",
                    imageMetadataList = images.collectAsLazyPagingItems(),
                    selectedImageIndex = 0,
                ),
                isCompact = false,
            )
        }
    }
}
