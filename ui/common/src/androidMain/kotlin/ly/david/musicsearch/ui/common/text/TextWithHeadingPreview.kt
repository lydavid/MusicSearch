package ly.david.musicsearch.ui.common.text

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewTextWithHeading() {
    PreviewTheme {
        Surface {
            TextWithHeading(
                heading = "Format",
                text = "Digital Media",
            )
        }
    }
}
