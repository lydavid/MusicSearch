package ly.david.musicsearch.ui.common.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.icons.MoreVert
import ly.david.musicsearch.ui.common.icons.StarFilled
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import ly.david.musicsearch.ui.common.text.TextWithIcon

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

@PreviewLightDark
@Composable
internal fun PreviewCollapsibleListSeparatorHeaderWithAdditionalContent() {
    PreviewTheme {
        Surface {
            CollapsibleListSeparatorHeader(
                text = "Listens",
                collapsed = false,
                additionalContent = {
                    TextWithIcon(
                        imageVector = CustomIcons.Headphones,
                        text = "99",
                        contentDescription = null,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                    TextWithIcon(
                        imageVector = CustomIcons.StarFilled,
                        text = "7",
                        contentDescription = null,
                        iconTint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                },
            )
        }
    }
}
