package ly.david.musicsearch.ui.common.release

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.FilterAlt
import ly.david.musicsearch.ui.common.screen.ReleaseStatusesScreen
import ly.david.musicsearch.ui.common.screen.showInBottomSheet
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverflowMenuScope.ShowStatusesMenuItem(
    browseMethod: BrowseMethod,
    coroutineScope: CoroutineScope,
    overlayHost: OverlayHost,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text("Filter statuses")
        },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.FilterAlt,
                contentDescription = null,
            )
        },
        onClick = {
            coroutineScope.launch {
                overlayHost.showInBottomSheet(
                    screen = ReleaseStatusesScreen(
                        browseMethod = browseMethod,
                    ),
                )
            }
        },
        modifier = modifier,
    )
}
