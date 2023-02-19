package ly.david.mbjc.ui.common.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Generic card with preset modifiers.
 */
@Composable
internal fun ClickableListItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        SelectionContainer {
            content()
        }
    }
}
