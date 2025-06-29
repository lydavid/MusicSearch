package ly.david.musicsearch.ui.common.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.window.DialogProperties
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.overlays.BasicDialogOverlay

suspend fun OverlayHost.showInDialog(
    screen: Screen,
): SnackbarPopResult = show(
    @OptIn(ExperimentalMaterial3Api::class)
    BasicDialogOverlay(
        model = Unit,
        properties = DialogProperties(
            usePlatformDefaultWidth = true,
        ),
        onDismissRequest = { SnackbarPopResult() },
    ) { _, overlayNavigator ->
        CircuitContent(
            screen = screen,
            onNavEvent = { event ->
                when (event) {
                    is NavEvent.Pop -> overlayNavigator.finish(event.result as SnackbarPopResult)
                    else -> {}
                }
            },
        )
    },
)
