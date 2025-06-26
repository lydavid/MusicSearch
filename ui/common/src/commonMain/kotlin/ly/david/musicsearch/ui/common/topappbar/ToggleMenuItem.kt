package ly.david.musicsearch.ui.common.topappbar

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
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    DropdownMenuItem(
        text = {
            Text(if (toggled) toggleOffText else toggleOnText)
        },
        leadingIcon = leadingIcon,
        onClick = {
            closeMenu()
            onToggle(!toggled)
        },
        modifier = modifier,
    )
}
