package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.ui.common.icons.BarChart
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.screen.showInDialog
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
fun OverflowMenuScope.StatsMenuItem(
    statsScreen: StatsScreen,
    overlayHost: OverlayHost,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    DropdownMenuItem(
        text = {
            Text(
                text = strings.stats,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.BarChart,
                contentDescription = null,
            )
        },
        onClick = {
            coroutineScope.launch {
                overlayHost.showInDialog(
                    screen = statsScreen,
                )
            }
            closeMenu()
        },
        modifier = modifier,
    )
}
