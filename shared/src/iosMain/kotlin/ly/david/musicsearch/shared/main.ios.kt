package ly.david.musicsearch.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.shared.di.sharedModule
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.core.theme.BaseTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

@Suppress("unused")
fun initKoin() {
    startKoin {
        modules(
            sharedModule,
        )
    }.koin
}

class AppComponent : KoinComponent {
    val circuit: Circuit by inject()
    val appPreferences: AppPreferences by inject()
}

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController =
    ComposeUIViewController {
        val appComponent = AppComponent()
        BaseTheme(
            darkTheme = appComponent.appPreferences.useDarkTheme(),
            materialYou = appComponent.appPreferences.useMaterialYou(),
        ) {
            val backStack: SaveableBackStack = rememberSaveableBackStack(
                initialScreens = persistentListOf(SearchScreen()),
            )
            val navigator: Navigator = rememberCircuitNavigator(
                backStack = backStack,
                onRootPop = {}
            )
            AppRoot(
                backStack = backStack,
                navigator = navigator,
                circuit = appComponent.circuit,
            )
        }
    }
