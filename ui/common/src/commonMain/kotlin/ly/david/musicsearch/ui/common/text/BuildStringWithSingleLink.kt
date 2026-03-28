package ly.david.musicsearch.ui.common.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun buildStringWithSingleLink(
    resourceWithPlaceholder: StringResource,
    linkLabel: String,
    url: String,
): AnnotatedString = buildAnnotatedString {
    val fullText = stringResource(resourceWithPlaceholder, linkLabel)
    val linkStartIndex = fullText.indexOf(linkLabel)
    val linkEndIndex = linkStartIndex + linkLabel.length

    append(fullText)

    addLink(
        url = LinkAnnotation.Url(
            url = url,
            styles = TextLinkStyles(
                style = SpanStyle(color = MaterialTheme.colorScheme.primary),
                hoveredStyle = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                ),
                pressedStyle = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                ),
            ),
        ),
        start = linkStartIndex,
        end = linkEndIndex,
    )
}
