import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import ly.david.musicsearch.shared.di.sharedModule
import ly.david.musicsearch.shared.screens.SettingsScreen
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
                CircuitCompositionLocals(circuit) {
                    val backStack = rememberSaveableBackStack(root = SettingsScreen)
                    val navigator = rememberCircuitNavigator(
                        backStack,
                        onRootPop = {},
                    )
                    NavigableCircuitContent(
                        navigator,
                        backStack,
                    )
                }
            },
        )
    }
}
