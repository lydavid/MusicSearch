package ly.david.musicsearch.shared.feature.images

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.test.image.InitializeFakeImageLoader
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCoverArtsPagerUiCompact() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsPagerUi(
                state = CoverArtsPagerUiState(
                    id = "a",
                    title = "Front",
                    subtitle = "1/2",
                    imageUrls = persistentListOf(
                        ImageUrls(
                            largeUrl = "https://www.example.com/blue.jpg",
                        ),
                        ImageUrls(
                            largeUrl = "https://www.example.com/blue.jpg",
                        ),
                    ),
                    selectedImageIndex = 0,
                ),
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
            CoverArtsPagerUi(
                state = CoverArtsPagerUiState(
                    id = "a",
                    title = "Front",
                    subtitle = "1/2",
                    imageUrls = persistentListOf(
                        ImageUrls(
                            largeUrl = "https://www.example.com/blue.jpg",
                        ),
                        ImageUrls(
                            largeUrl = "https://www.example.com/blue.jpg",
                        ),
                    ),
                    selectedImageIndex = 0,
                ),
            )
        }
    }
}
