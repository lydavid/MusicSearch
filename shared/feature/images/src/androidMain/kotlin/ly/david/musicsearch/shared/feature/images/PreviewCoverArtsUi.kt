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
internal fun PreviewCoverArtsGrid() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsUi(
                state = CoverArtsUiState(
                    id = "a",
                    title = "Cover arts",
                    imageUrls = persistentListOf(
                        ImageUrls(
                            thumbnailUrl = "https://www.example.com/blue.jpg",
                        ),
                        ImageUrls(
                            thumbnailUrl = "https://www.example.com/red.jpg",
                        ),
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCoverArtsPagerCompact() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsUi(
                state = CoverArtsUiState(
                    id = "a",
                    title = "Front",
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
                isCompact = true,
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewCoverArtsPagerNonCompact() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsUi(
                state = CoverArtsUiState(
                    id = "a",
                    title = "Front",
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
