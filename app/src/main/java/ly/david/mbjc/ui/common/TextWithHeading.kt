package ly.david.mbjc.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

// TODO: selecting this starting from heading will behave strangely for text with multiple lines
@Composable
internal fun TextWithHeading(
    @StringRes headingRes: Int,
    text: String
) {
    SelectionContainer {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${stringResource(id = headingRes)}: ",
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = text,
                style = TextStyles.getCardBodyTextStyle(),
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            TextWithHeading(headingRes = R.string.format, text = "Digital Media")
        }
    }
}
