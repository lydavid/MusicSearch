package ly.david.ui.common.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
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

@DefaultPreviews
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

@DefaultPreviews
@Composable
internal fun PreviewClickableItemWithIcon() {
    PreviewTheme {
        Surface {
            ClickableItem(
                title = "Click me, my text is so long it will possibly push the end icon off the screen",
                endIcon = Icons.Default.ChevronRight,
            )
        }
    }
}
