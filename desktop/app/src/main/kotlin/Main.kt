import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.shared.AppRoot
import ly.david.musicsearch.shared.di.sharedModule
import ly.david.musicsearch.shared.useDarkTheme
import ly.david.musicsearch.shared.useMaterialYou
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.core.theme.BaseTheme
import org.koin.core.context.startKoin

fun main() = application {
    val windowState = rememberWindowState()

    val koin = startKoin {
        modules(
            sharedModule,
        )
    }.koin

    val circuit: Circuit = koin.get()
    val appPreferences: AppPreferences = koin.get()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "MusicSearch",
    ) {
        BaseTheme(
            darkTheme = appPreferences.useDarkTheme(),
            materialYou = appPreferences.useMaterialYou(),
            content = {
                val backStack: SaveableBackStack = rememberSaveableBackStack(
                    initialScreens = persistentListOf(SearchScreen()),
                )
                val navigator: Navigator = rememberCircuitNavigator(
                    backStack = backStack,
                    onRootPop = { exitApplication() },
                )

                AppRoot(
                    backStack = backStack,
                    navigator = navigator,
                    circuit = circuit,
                )
            },
        )
    }
}
