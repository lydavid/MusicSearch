package ly.david.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
fun OverflowMenuScope.AddToCollectionMenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val strings = LocalStrings.current

    DropdownMenuItem(
        text = {
            Text(strings.addToCollection)
        },
        onClick = {
            onClick()
            closeMenu()
        },
        modifier = modifier,
    )
}
