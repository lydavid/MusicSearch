package ly.david.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.LocalOverlayHost
import ly.david.musicsearch.strings.LocalStrings

@Composable
fun OverflowMenuScope.AddToCollectionMenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val strings = LocalStrings.current
    val overlayHost = LocalOverlayHost.current

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
