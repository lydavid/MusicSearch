package ly.david.musicsearch.ui.common.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun TextWithHeading(
    heading: String,
    text: String,
    modifier: Modifier = Modifier,
    filterText: String = "",
) {
    if (heading.contains(filterText, ignoreCase = true) ||
        text.contains(filterText, ignoreCase = true)
    ) {
        SelectionContainer {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("$heading: ")
                    }
                    append(text)
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 4.dp,
                    ),
                style = TextStyles.getCardBodyTextStyle(),
            )
        }
    }
}
