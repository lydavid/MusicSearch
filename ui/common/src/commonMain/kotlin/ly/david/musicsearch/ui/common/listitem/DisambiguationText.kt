package ly.david.musicsearch.ui.common.listitem

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.ui.core.theme.TextStyles
import ly.david.musicsearch.ui.core.theme.getSubTextColor

/**
 * Displays [disambiguation] if it exists.
 */
@Composable
fun DisambiguationText(
    disambiguation: String?,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
) {
    disambiguation.ifNotNullOrEmpty {
        Text(
            text = "($it)",
            modifier = modifier.padding(top = 4.dp),
            color = getSubTextColor(),
            style = TextStyles.getCardBodyTextStyle(),
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
