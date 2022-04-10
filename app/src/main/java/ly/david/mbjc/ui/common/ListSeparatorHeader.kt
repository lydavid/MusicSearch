package ly.david.mbjc.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubBackgroundColor

@Composable
fun ListSeparatorHeader(text: String) {
    Surface(color = getSubBackgroundColor()) {
        Text(
            text = text,
            style = TextStyles.getCardBodyTextStyle(),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth()
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ListSeparatorHeaderPreview() {
    PreviewTheme {
        ListSeparatorHeader("Album + Compilation")
    }
}
