package ly.david.musicsearch.ui.common.listitem

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCollapsibleListSeparatorHeaderCollapsed() {
    PreviewTheme {
        Surface {
            CollapsibleListSeparatorHeader(
                text = "Album + Compilation",
                collapsed = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollapsibleListSeparatorHeaderExpanded() {
    PreviewTheme {
        Surface {
            CollapsibleListSeparatorHeader(
                text = "Album + Compilation",
                collapsed = false,
            )
        }
    }
}
