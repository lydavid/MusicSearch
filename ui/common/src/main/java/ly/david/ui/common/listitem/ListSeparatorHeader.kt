package ly.david.ui.common.listitem

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.ui.common.R
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun ListSeparatorHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant
    Surface(
        modifier = modifier,
        color = surfaceColor
    ) {
        SelectionContainer {
            Text(
                text = text,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                color = contentColorFor(backgroundColor = surfaceColor),
                style = TextStyles.getCardBodyTextStyle()
            )
        }
    }
}

@Composable
fun AttributesListSeparatorHeader(@StringRes resourceStringRes: Int) {
    ListSeparatorHeader(text = stringResource(id = R.string.attributes_header, stringResource(id = resourceStringRes)))
}

@Composable
fun InformationListSeparatorHeader(@StringRes resourceStringRes: Int) {
    ListSeparatorHeader(text = stringResource(id = R.string.information_header, stringResource(id = resourceStringRes)))
}

@DefaultPreviews
@Composable
private fun ListSeparatorHeaderPreview() {
    PreviewTheme {
        Surface {
            ListSeparatorHeader("Album + Compilation")
        }
    }
}

@DefaultPreviews
@Composable
private fun AttributesListSeparatorHeaderPreview() {
    PreviewTheme {
        Surface {
            AttributesListSeparatorHeader(R.string.work)
        }
    }
}

@DefaultPreviews
@Composable
private fun InformationListSeparatorHeaderPreview() {
    PreviewTheme {
        Surface {
            InformationListSeparatorHeader(R.string.area)
        }
    }
}
