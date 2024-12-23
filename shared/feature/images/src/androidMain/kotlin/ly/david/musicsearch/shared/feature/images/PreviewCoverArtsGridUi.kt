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
internal fun PreviewCoverArtsGridUi() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsGridUi(
                state = CoverArtsGridUiState(
                    id = "a",
                    title = "Cover arts",
                    imageUrls = persistentListOf(
                        ImageUrls(
                            databaseId = 1,
                            thumbnailUrl = "https://www.example.com/blue.jpg",
                            largeUrl = "https://www.example.com/blue.jpg",
                            types = persistentListOf("Front"),
                            comment = "",
                        ),
                        ImageUrls(
                            databaseId = 2,
                            thumbnailUrl = "https://www.example.com/red.jpg",
                            largeUrl = "https://www.example.com/red.jpg",
                            types = persistentListOf("Back"),
                            comment = "",
                        ),
                    ),
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
                state = CoverArtsGridUiState(
                    id = "a",
                    title = "Front",
                    subtitle = "1/2",
                    imageUrls = persistentListOf(
                        ImageUrls(
                            databaseId = 1,
                            thumbnailUrl = "https://www.example.com/blue.jpg",
                            largeUrl = "https://www.example.com/blue.jpg",
                            types = persistentListOf("Front"),
                            comment = "",
                        ),
                        ImageUrls(
                            databaseId = 2,
                            thumbnailUrl = "https://www.example.com/red.jpg",
                            largeUrl = "https://www.example.com/red.jpg",
                            types = persistentListOf("Back"),
                            comment = "",
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
internal fun PreviewCoverArtsPagerUiNonCompact() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsGridUi(
                state = CoverArtsGridUiState(
                    id = "a",
                    title = "Front",
                    subtitle = "1/2",
                    imageUrls = persistentListOf(
                        ImageUrls(
                            databaseId = 1,
                            thumbnailUrl = "https://www.example.com/blue.jpg",
                            largeUrl = "https://www.example.com/blue.jpg",
                            types = persistentListOf("Front"),
                            comment = "",
                        ),
                        ImageUrls(
                            databaseId = 2,
                            thumbnailUrl = "https://www.example.com/red.jpg",
                            largeUrl = "https://www.example.com/red.jpg",
                            types = persistentListOf("Back"),
                            comment = "",
                        ),
                    ),
                    selectedImageIndex = 0,
                ),
                isCompact = false,
            )
        }
    }
}
