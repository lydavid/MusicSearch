package ly.david.ui.common.topappbar

import androidx.annotation.StringRes
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun OverflowMenuScope.ToggleMenuItem(
    @StringRes toggleOnText: Int,
    @StringRes toggleOffText: Int,
    toggled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text(stringResource(id = if (toggled) toggleOffText else toggleOnText))
        },
        onClick = {
            closeMenu()
            onToggle(!toggled)
        },
        modifier = modifier
    )
}
