package ly.david.musicsearch.ui.common.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.MoreVert
import ly.david.musicsearch.ui.common.preview.PreviewTheme

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

@PreviewLightDark
@Composable
internal fun PreviewCollapsibleListSeparatorHeaderWithEndContent() {
    PreviewTheme {
        Surface {
            CollapsibleListSeparatorHeader(
                text = "Genres",
                collapsed = false,
                endContent = {
                    Icon(
                        imageVector = CustomIcons.MoreVert,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { },
                    )
                },
            )
        }
    }
}
