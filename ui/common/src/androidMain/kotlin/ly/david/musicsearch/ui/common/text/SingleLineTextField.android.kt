package ly.david.musicsearch.ui.common.text

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@Preview(device = "spec:width=1080px,height=600px,dpi=440,orientation=portrait")
@PreviewLightDark
@Composable
internal fun PreviewSingleLineTextFieldEmpty() {
    PreviewTheme {
        Surface {
            SingleLineTextField(
                text = "",
                textLabel = "MusicBrainz URL/MBID",
                textHint = "MusicBrainz URL/MBID",
                onTextChange = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSingleLineTextFieldWithText() {
    PreviewTheme {
        Surface {
            SingleLineTextField(
                text = "https://musicbrainz.org/release/b13dc076-b9c3-4365-9555-807d80acfd67",
                textLabel = "MusicBrainz URL/MBID",
                textHint = "MusicBrainz URL/MBID",
                onTextChange = {},
            )
        }
    }
}
