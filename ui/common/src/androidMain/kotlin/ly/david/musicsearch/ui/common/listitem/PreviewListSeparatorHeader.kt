package ly.david.musicsearch.ui.common.listitem

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewListSeparatorHeader() {
    PreviewTheme {
        Surface {
            ListSeparatorHeader(
                text = "Album + Compilation",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListSeparatorHeaderWithUnknownNumberOfImages() {
    PreviewTheme {
        Surface {
            ListSeparatorHeader(
                text = "Release information",
                numberOfImages = null,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListSeparatorHeaderWithZeroImages() {
    PreviewTheme {
        Surface {
            ListSeparatorHeader(
                text = "Release information",
                numberOfImages = 0,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListSeparatorHeaderWithOneImage() {
    PreviewTheme {
        Surface {
            ListSeparatorHeader(
                text = "Release information",
                numberOfImages = 1,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListSeparatorHeaderWithMultipleImages() {
    PreviewTheme {
        Surface {
            ListSeparatorHeader(
                text = "Release information",
                numberOfImages = 2,
            )
        }
    }
}
