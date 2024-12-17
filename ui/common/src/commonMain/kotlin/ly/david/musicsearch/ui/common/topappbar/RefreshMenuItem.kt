package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
fun OverflowMenuScope.RefreshMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    DropdownMenuItem(
        text = {
            Text(strings.refresh)
        },
        onClick = {
            onClick()
            closeMenu()
        },
        modifier = modifier,
    )
}
