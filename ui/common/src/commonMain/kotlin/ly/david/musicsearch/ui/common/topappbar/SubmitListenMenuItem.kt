package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.HeadphonesAdd
import ly.david.musicsearch.ui.common.screen.SubmitListenScreen
import ly.david.musicsearch.ui.common.screen.showInDialog
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.submitListen
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverflowMenuScope.SubmitListenMenuItem(
    submitListenType: SubmitListenType,
    overlayHost: OverlayHost,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        text = {
            Text(text = stringResource(Res.string.submitListen))
        },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.HeadphonesAdd,
                contentDescription = null,
            )
        },
        onClick = {
            coroutineScope.launch {
                val result = overlayHost.showInDialog(
                    screen = SubmitListenScreen(
                        submitListenType = submitListenType,
                    ),
                )
                result.message.ifNotNullOrEmpty {
                    snackbarHostState.showSnackbar(
                        message = result.message,
                        actionLabel = result.actionLabel,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true,
                    )
                }
            }
            closeMenu()
        },
        modifier = modifier,
    )
}
