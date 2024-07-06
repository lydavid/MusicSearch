package ly.david.musicsearch.shared.feature.images

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.models.image.ImageUrls
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme
import ly.david.musicsearch.ui.test.screenshot.InitializeFakeImageLoader

@PreviewLightDark
@Composable
internal fun PreviewCoverArtsUiCompact() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsUi(
                isCompact = true,
                imageUrlsList = persistentListOf(
                    ImageUrls(
                        largeUrl = "https://www.example.com/image.jpg",
                    ),
                    ImageUrls(
                        largeUrl = "https://www.example.com/image.jpg",
                    ),
                ),
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewCoverArtsUiNonCompact() {
    InitializeFakeImageLoader()
    PreviewTheme {
        Surface {
            CoverArtsUi(
                imageUrlsList = persistentListOf(
                    ImageUrls(
                        largeUrl = "https://www.example.com/image.jpg",
                    ),
                    ImageUrls(
                        largeUrl = "https://www.example.com/image.jpg",
                    ),
                ),
            )
        }
    }
}
