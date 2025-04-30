package ly.david.musicsearch.ui.common.listitem

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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.ArrowDropDown
import ly.david.musicsearch.ui.common.icons.ArrowDropUp
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
fun CollapsibleListSeparatorHeader(
    text: String,
    collapsed: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
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
                imageVector = if (collapsed) CustomIcons.ArrowDropUp else CustomIcons.ArrowDropDown,
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
