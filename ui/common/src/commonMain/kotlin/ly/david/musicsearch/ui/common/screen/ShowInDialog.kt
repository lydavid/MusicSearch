package ly.david.musicsearch.ui.common.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.window.DialogProperties
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.overlays.BasicDialogOverlay
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable

@Suppress("UNCHECKED_CAST")
suspend fun <T : CommonParcelable> OverlayHost.showInDialogForResult(
    screen: Screen,
): SnackbarPopResultV2<T> = show(
    @OptIn(ExperimentalMaterial3Api::class)
    BasicDialogOverlay(
        model = Unit,
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
        ),
        onDismissRequest = { SnackbarPopResultV2(feedback = null) },
    ) { _, overlayNavigator ->
        CircuitContent(
            screen = screen,
            onNavEvent = { event ->
                when (event) {
                    is NavEvent.Pop -> overlayNavigator.finish(event.result as SnackbarPopResultV2<T>)
                    else -> {}
                }
            },
        )
    },
)

suspend fun OverlayHost.showInDialog(
    screen: Screen,
): PopWithoutResult = show(
    @OptIn(ExperimentalMaterial3Api::class)
    BasicDialogOverlay(
        model = Unit,
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
        ),
        onDismissRequest = { PopWithoutResult },
    ) { _, overlayNavigator ->
        CircuitContent(
            screen = screen,
            onNavEvent = { event ->
                when (event) {
                    is NavEvent.Pop -> overlayNavigator.finish(PopWithoutResult)
                    else -> {}
                }
            },
        )
    },
)
