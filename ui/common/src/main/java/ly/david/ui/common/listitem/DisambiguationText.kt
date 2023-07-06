package ly.david.ui.common.listitem

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles
import ly.david.ui.core.theme.getSubTextColor

/**
 * Displays [disambiguation] if it exists.
 */
@Composable
fun DisambiguationText(
    disambiguation: String?,
    modifier: Modifier = Modifier,
) {
    disambiguation.ifNotNullOrEmpty {
        Text(
            text = "($it)",
            modifier = modifier.padding(top = 4.dp),
            color = getSubTextColor(),
            style = TextStyles.getCardBodyTextStyle()
        )
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            DisambiguationText(disambiguation = "Disambiguation text")
        }
    }
}
