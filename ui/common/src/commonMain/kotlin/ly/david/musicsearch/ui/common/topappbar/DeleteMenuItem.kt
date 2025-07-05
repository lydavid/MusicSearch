package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.DeleteOutline

@Composable
fun OverflowMenuScope.DeleteMenuItem(
    selectionState: SelectionState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text(
                text = "Delete ${selectionState.selectedIds.size}",
                color = MaterialTheme.colorScheme.error,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.DeleteOutline,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null,
            )
        },
        onClick = {
            closeMenu()
            onClick()
        },
        modifier = modifier,
    )
}
