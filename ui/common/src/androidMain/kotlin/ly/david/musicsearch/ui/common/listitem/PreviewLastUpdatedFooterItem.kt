package ly.david.musicsearch.ui.common.listitem

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import kotlin.time.Instant

@PreviewLightDark
@Composable
internal fun PreviewLastUpdatedFooterItem() {
    PreviewTheme {
        Surface {
            LastUpdatedFooterItem(
                lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
                now = Instant.parse("2025-04-26T16:42:20Z"),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLastUpdatedText() {
    PreviewTheme {
        Surface {
            LastUpdatedText(
                lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
                now = Instant.parse("2025-04-26T16:42:20Z"),
            )
        }
    }
}
