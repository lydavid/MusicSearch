package ly.david.musicsearch.ui.common.text

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@DefaultPreviews
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
