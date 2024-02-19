package ly.david.ui.common.listitem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.ui.core.theme.TextStyles

@Composable
fun ListSeparatorHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    Surface(
        modifier = modifier,
        color = surfaceColor,
    ) {
        SelectionContainer {
            Text(
                text = text,
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 4.dp,
                    )
                    .fillMaxWidth(),
                color = contentColorFor(backgroundColor = surfaceColor),
                style = TextStyles.getCardBodyTextStyle(),
            )
        }
    }
}
