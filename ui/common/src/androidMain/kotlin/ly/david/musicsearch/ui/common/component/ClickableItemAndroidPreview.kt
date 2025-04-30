package ly.david.musicsearch.ui.common.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Person
import ly.david.musicsearch.ui.common.icons.Search
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewClickableItem() {
    PreviewTheme {
        Surface {
            ClickableItem(
                title = "Click me",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewClickableItemWithSubtitle() {
    PreviewTheme {
        Surface {
            ClickableItem(
                title = "Click me",
                subtitle = "This is why you should click me",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewClickableItemWithEndIcon() {
    PreviewTheme {
        Surface {
            ClickableItem(
                title = "Click me, my text is so long it will possibly push the end icon off the screen",
                endIcon = CustomIcons.ChevronRight,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewClickableItemWithStartIcon() {
    PreviewTheme {
        Surface {
            ClickableItem(
                title = "Click me, my text is so long it will possibly push the start icon off the screen",
                startIcon = CustomIcons.Person,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewClickableItemWithStartEndIcon() {
    PreviewTheme {
        Surface {
            ClickableItem(
                title = "Click me, my text is so long it will possibly push the start icon off the screen",
                startIcon = CustomIcons.Person,
                endIcon = CustomIcons.Search,
            )
        }
    }
}
