package ly.david.musicsearch.ui.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun ClickableItem(
    title: String,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    filterText: String = "",
    subtitle: String? = null,
    startIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    foregroundColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {},
) {
    val startIconComposable: @Composable (BoxScope.() -> Unit)? = if (startIcon == null) {
        null
    } else {
        {
            Icon(
                imageVector = startIcon,
                contentDescription = null,
                modifier = iconModifier.align(Alignment.CenterStart),
                tint = foregroundColor,
            )
        }
    }

    ClickableItem(
        title = title,
        modifier = modifier,
        iconModifier = iconModifier,
        filterText = filterText,
        subtitle = subtitle,
        startIcon = startIconComposable,
        endIcon = endIcon,
        fontWeight = fontWeight,
        foregroundColor = foregroundColor,
        onClick = onClick,
    )
}

@Composable
fun ClickableItem(
    title: String,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    filterText: String = "",
    subtitle: String? = null,
    startIcon: @Composable (BoxScope.() -> Unit)?,
    endIcon: ImageVector? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    foregroundColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
    ) {
        startIcon?.invoke(this)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = if (startIcon == null) 0.dp else 32.dp,
                    end = if (endIcon == null) 0.dp else 32.dp,
                ),
        ) {
            HighlightableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = fontWeight)) {
                        append(title)
                    }
                },
                highlightedText = filterText,
                style = TextStyles.getCardBodyTextStyle(),
                foregroundColor = foregroundColor,
            )

            subtitle?.let {
                Text(
                    text = subtitle,
                    style = TextStyles.getCardBodySubTextStyle(),
                    fontWeight = fontWeight,
                    color = foregroundColor,
                )
            }
        }

        endIcon?.let {
            Icon(
                imageVector = endIcon,
                contentDescription = null,
                modifier = iconModifier.align(Alignment.CenterEnd),
                tint = foregroundColor,
            )
        }
    }
}
