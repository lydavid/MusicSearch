import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.slack.circuit.foundation.Circuit
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.shared.AppRoot
import ly.david.musicsearch.shared.di.sharedModule
import ly.david.musicsearch.shared.useDarkTheme
import ly.david.musicsearch.shared.useMaterialYou
import ly.david.ui.common.screen.SearchScreen
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
                AppRoot(
                    circuit = circuit,
                    initialScreens = persistentListOf(SearchScreen()),
                )
            },
        )
    }
}
