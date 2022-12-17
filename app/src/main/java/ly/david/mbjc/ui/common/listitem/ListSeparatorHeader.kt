package ly.david.mbjc.ui.common.listitem

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubBackgroundColor

@Composable
internal fun ListSeparatorHeader(text: String) {
    Surface(color = getSubBackgroundColor()) {
        SelectionContainer {
            Text(
                text = text,
                style = TextStyles.getCardBodyTextStyle(),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
internal fun InformationListSeparatorHeader(@StringRes resourceStringRes: Int) {
    ListSeparatorHeader(text = stringResource(id = R.string.information_header, stringResource(id = resourceStringRes)))
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun ListSeparatorHeaderPreview() {
    PreviewTheme {
        ListSeparatorHeader("Album + Compilation")
    }
}
