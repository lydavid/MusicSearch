package ly.david.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ly.david.ui.common.R

@Composable
fun OverflowMenuScope.AddToCollectionMenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(stringResource(id = R.string.add_to_collection))
        },
        onClick = {
            onClick()
            closeMenu()
        },
        modifier = modifier
    )
}
