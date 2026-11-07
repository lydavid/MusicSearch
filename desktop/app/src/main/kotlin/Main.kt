import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.AppRoot
import ly.david.musicsearch.shared.di.sharedModule
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.useDarkTheme
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.common.theme.BaseTheme
import org.koin.core.context.startKoin

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
            appPreferences = appPreferences,
            darkTheme = appPreferences.useDarkTheme(),
            content = {
                val backStack: SaveableBackStack = rememberSaveableBackStack(
                    initialScreens = persistentListOf(SearchScreen()),
                )
                val navigator: Navigator = rememberCircuitNavigator(
                    backStack = backStack,
                    onRootPop = { exitApplication() },
                )

                val windowSizeClass = calculateWindowSizeClass()

                AppRoot(
                    backStack = backStack,
                    navigator = navigator,
                    circuit = circuit,
                    isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
                )
            },
        )
    }
}
