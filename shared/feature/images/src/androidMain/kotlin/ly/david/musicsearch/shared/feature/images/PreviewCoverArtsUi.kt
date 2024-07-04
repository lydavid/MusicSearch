package ly.david.musicsearch.shared.feature.images

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.models.image.ImageUrls
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCoverArtsUiOne() {
    PreviewTheme {
        Surface {
            CoverArtsUi(
                imageUrlsList = persistentListOf(
                    ImageUrls(
                        largeUrl = "https://www.example.com/image.jpg",
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCoverArtsUiMultiple() {
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
