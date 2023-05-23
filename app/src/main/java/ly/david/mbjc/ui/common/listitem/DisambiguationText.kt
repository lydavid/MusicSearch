package ly.david.mbjc.ui.common.listitem

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.ui.common.theme.TextStyles
import ly.david.ui.common.theme.getSubTextColor

/**
 * Displays [disambiguation] if it exists.
 */
@Composable
internal fun DisambiguationText(disambiguation: String?) {
    disambiguation.ifNotNullOrEmpty {
        Text(
            text = "($it)",
            modifier = Modifier.padding(top = 4.dp),
            color = getSubTextColor(),
            style = TextStyles.getCardBodyTextStyle()
        )
    }
}
