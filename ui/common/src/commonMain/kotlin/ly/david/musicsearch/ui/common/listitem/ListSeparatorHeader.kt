package ly.david.musicsearch.ui.common.listitem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun ListSeparatorHeader(
    text: String,
    modifier: Modifier = Modifier,
    endContent: @Composable (() -> Unit)? = null,
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    Surface(
        modifier = modifier,
        color = surfaceColor,
    ) {
        SelectionContainer {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp,
                        ),
                    color = contentColorFor(backgroundColor = surfaceColor),
                    style = TextStyles.getCardBodyTextStyle(),
                )
                Spacer(modifier = Modifier.weight(1f))
                endContent?.invoke()
            }
        }
    }
}
