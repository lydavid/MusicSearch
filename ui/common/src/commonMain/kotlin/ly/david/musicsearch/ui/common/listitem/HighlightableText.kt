package ly.david.musicsearch.ui.common.listitem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun HighlightableText(
    text: String,
    highlightedText: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    val annotatedString = buildAnnotatedString {
        if (highlightedText.isEmpty()) {
            append(text)
            return@buildAnnotatedString
        }

        var currentIndex = 0
        val searchText = text.lowercase()
        val searchHighlight = highlightedText.lowercase()

        while (currentIndex < text.length) {
            val index = searchText.indexOf(searchHighlight, currentIndex)
            if (index == -1) {
                append(text.substring(currentIndex))
                break
            }

            append(text.substring(currentIndex, index))

            withStyle(
                style = SpanStyle(
                    background = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                ),
            ) {
                append(text.substring(index, index + highlightedText.length))
            }

            currentIndex = index + highlightedText.length
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        style = TextStyles.getCardBodyTextStyle(),
        fontWeight = fontWeight,
    )
}
