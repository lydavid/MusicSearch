package ly.david.musicsearch.ui.common.screen

import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.overlays.BottomSheetOverlay

suspend fun OverlayHost.showInBottomSheet(
    screen: Screen,
): Unit = show(
    BottomSheetOverlay(
        model = Unit,
        onDismiss = {}, // Crashes if we don't include this
    ) { _, overlayNavigator ->
        CircuitContent(
            screen = screen,
            onNavEvent = { event ->
                when (event) {
                    is NavEvent.Pop -> overlayNavigator.finish(Unit)
                    else -> {}
                }
            },
        )
    },
)
