import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import ly.david.musicsearch.shared.di.sharedModule
import ly.david.ui.common.screen.SearchScreen
import ly.david.ui.core.theme.BaseTheme
import org.koin.core.context.startKoin

fun main() = application {
    val windowState = rememberWindowState()

    val koin = startKoin {
        modules(
            sharedModule,
        )
    }.koin

    val circuit = koin.get<Circuit>()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "MusicSearch",
    ) {
        BaseTheme(
            content = {
                ContentWithOverlays {
                    CircuitCompositionLocals(circuit) {
                        val backStack = rememberSaveableBackStack(root = SearchScreen())
                        val navigator = rememberCircuitNavigator(
                            backStack,
                            onRootPop = {},
                        )
                        NavigableCircuitContent(
                            navigator,
                            backStack,
                        )
                    }
                }
            },
        )
    }
}
