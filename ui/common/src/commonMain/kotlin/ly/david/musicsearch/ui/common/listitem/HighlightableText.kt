package ly.david.musicsearch.ui.common.listitem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun HighlightableText(
    text: String,
    highlightedText: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    style: TextStyle = TextStyles.getCardBodyTextStyle(),
    textAlign: TextAlign? = null,
    foregroundColor: Color = Color.Unspecified,
) {
    HighlightableText(
        text = AnnotatedString(text),
        highlightedText = highlightedText,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow,
        style = style,
        textAlign = textAlign,
        foregroundColor = foregroundColor,
    )
}

@Composable
fun HighlightableText(
    text: AnnotatedString,
    highlightedText: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    style: TextStyle = TextStyles.getCardBodyTextStyle(),
    textAlign: TextAlign? = null,
    foregroundColor: Color = Color.Unspecified,
) {
    val annotatedString = buildAnnotatedString {
        if (highlightedText.isEmpty()) {
            append(text)
            return@buildAnnotatedString
        }

        var currentIndex = 0
        val searchText = text.text.lowercase()
        val searchHighlight = highlightedText.lowercase()

        while (currentIndex < text.length) {
            val index = searchText.indexOf(searchHighlight, currentIndex)
            if (index == -1) {
                append(text.subSequence(currentIndex, text.length))
                break
            }

            append(text.subSequence(currentIndex, index))

            withStyle(
                style = SpanStyle(
                    background = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                ),
            ) {
                append(text.subSequence(index, index + highlightedText.length))
            }

            currentIndex = index + highlightedText.length
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        style = style,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
        color = foregroundColor,
    )
}
