package ly.david.mbjc.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Generic card with preset modifiers.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClickableListItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    ListItem(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
    ) {
        content()
    }
}
