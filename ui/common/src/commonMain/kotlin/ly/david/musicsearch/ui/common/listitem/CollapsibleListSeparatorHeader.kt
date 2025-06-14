package ly.david.musicsearch.ui.common.listitem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.ArrowDropDown
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.theme.TextStyles

private const val COLLAPSED_ROTATION_DEGREE = -90f

@Composable
fun CollapsibleListSeparatorHeader(
    text: String,
    collapsed: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val iconRotationDegree = if (collapsed) COLLAPSED_ROTATION_DEGREE else 0f
    val animatedRotationDegree = animateFloatAsState(targetValue = iconRotationDegree)

    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    Surface(
        modifier = modifier.clickable {
            onClick()
        },
        color = surfaceColor,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = 4.dp,
                )
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {
                    contentDescription = "$text, ${if (collapsed) "collapsed" else "expanded"}"
                },
        ) {
            Icon(
                imageVector = CustomIcons.ArrowDropDown,
                modifier = Modifier
                    .rotate(animatedRotationDegree.value),
                contentDescription = null,
            )

            Text(
                text = text,
                modifier = Modifier
                    .padding(start = 4.dp),
                color = contentColorFor(backgroundColor = surfaceColor),
                style = TextStyles.getCardBodyTextStyle(),
            )
        }
    }
}
