package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.HeadphonesAdd
import ly.david.musicsearch.ui.common.screen.SubmitListenScreen
import ly.david.musicsearch.ui.common.screen.showInDialog

@Composable
fun OverflowMenuScope.SubmitListenMenuItem(
    submitListenType: SubmitListenType,
    overlayHost: OverlayHost,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text(text = "Submit listen")
        },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.HeadphonesAdd,
                contentDescription = null,
            )
        },
        onClick = {
            coroutineScope.launch {
                overlayHost.showInDialog(
                    screen = SubmitListenScreen(
                        submitListenType = submitListenType,
                    ),
                )
            }
            closeMenu()
        },
        modifier = modifier,
    )
}
