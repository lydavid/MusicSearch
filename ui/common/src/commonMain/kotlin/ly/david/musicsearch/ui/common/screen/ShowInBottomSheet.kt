package ly.david.musicsearch.ui.common.screen

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.overlay.OverlayHost
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.overlays.BottomSheetOverlay

suspend fun OverlayHost.showInBottomSheet(
    screen: Screen,
): SnackbarPopResult = show(
    @OptIn(ExperimentalMaterial3Api::class)
    BottomSheetOverlay(
        model = Unit,
        onDismiss = { SnackbarPopResult() },
    ) { _, overlayNavigator ->
        CircuitContent(
            screen = screen,
            modifier = Modifier.navigationBarsPadding(),
            onNavEvent = { event ->
                when (event) {
                    is NavEvent.Pop -> overlayNavigator.finish(event.result as SnackbarPopResult)
                    else -> {}
                }
            },
        )
    },
)
