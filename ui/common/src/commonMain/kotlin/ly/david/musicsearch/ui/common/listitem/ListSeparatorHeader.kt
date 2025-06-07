package ly.david.musicsearch.ui.common.listitem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.PhotoLibrary
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

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
                            vertical = 4.dp,
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

@Composable
fun ListSeparatorHeader(
    text: String,
    numberOfImages: Int?,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val endContent: @Composable (() -> Unit)? = if (numberOfImages == null) {
        null
    } else {
        {
            Surface(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 8.dp,
                        )
                        .padding(
                            top = 4.dp,
                            bottom = 3.dp,
                        )
                        .semantics(mergeDescendants = true) {},
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = CustomIcons.PhotoLibrary,
                        contentDescription = strings.numberOfImages,
                    )
                    Text(
                        text = numberOfImages.toString(),
                        modifier = Modifier
                            .padding(start = 4.dp),
                    )
                }
            }
        }
    }
    ListSeparatorHeader(
        text = text,
        modifier = modifier,
        endContent = endContent,
    )
}
