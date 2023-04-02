package ly.david.mbjc.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ly.david.mbjc.R

@Composable
internal fun OverflowMenuScope.AddToCollectionMenuItem(onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Text(stringResource(id = R.string.add_to_collection))
        },
        onClick = {
            onClick()
            closeMenu()
        }
    )
}
