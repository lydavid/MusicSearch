package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun OverflowMenuScope.GoToScreenMenuItem(
    text: String,
    leadingIconImageVector: ImageVector,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Text(text = text)
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIconImageVector,
                contentDescription = null,
            )
        },
        onClick = {
            closeMenu()
            onClick()
        },
    )
}
