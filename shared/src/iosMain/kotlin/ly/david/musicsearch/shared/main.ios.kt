package ly.david.musicsearch.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.slack.circuit.foundation.Circuit
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
            AppRoot(
                circuit = appComponent.circuit,
                initialScreens = persistentListOf(SearchScreen()),
            )
        }
    }
