package ly.david.ui.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun ClickableItem(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    endIcon: ImageVector? = null,
    onClick: () -> Unit = {},
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
    ) {
        val (text, icon) = createRefs()

        Column(
            modifier = Modifier.constrainAs(text) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(icon.start)
            },
        ) {
            Text(
                text = title,
                style = TextStyles.getCardBodyTextStyle(),
            )

            subtitle?.let {
                Text(
                    text = subtitle,
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        }

        Box(
            modifier = Modifier.constrainAs(icon) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },
        ) {
            endIcon?.let {
                Icon(
                    imageVector = endIcon,
                    contentDescription = null,
                )
            }
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
                title = "Click me, my text is so long it will possibly push the end icon off the screen",
                endIcon = Icons.Default.ChevronRight,
            )
        }
    }
}
// endregion
