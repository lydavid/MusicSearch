package ly.david.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OverflowMenuScope.ToggleMenuItem(
    toggleOnText: String,
    toggleOffText: String,
    toggled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text(if (toggled) toggleOffText else toggleOnText)
        },
        onClick = {
            closeMenu()
            onToggle(!toggled)
        },
        modifier = modifier,
    )
}
