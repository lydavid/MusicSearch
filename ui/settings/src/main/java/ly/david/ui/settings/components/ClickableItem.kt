package ly.david.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun ClickableItem(
    title: String,
    subtitle: String? = null,
    endIcon: ImageVector? = null,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = title,
                style = TextStyles.getCardBodyTextStyle()
            )

            subtitle?.let {
                Text(
                    text = subtitle,
                    style = TextStyles.getCardBodySubTextStyle()
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        endIcon?.let {
            Icon(
                imageVector = endIcon,
                contentDescription = null,
            )
        }
    }
}

// region Previews
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
                title = "Click me",
                endIcon = Icons.Default.ChevronRight,
            )
        }
    }
}
// endregion
