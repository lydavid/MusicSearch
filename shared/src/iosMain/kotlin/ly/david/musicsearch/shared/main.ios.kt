package ly.david.musicsearch.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.slack.circuit.foundation.Circuit
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.shared.di.sharedModule
import ly.david.ui.core.theme.BaseTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun initKoin() {
    val koin = startKoin {
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
        BaseTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.primary,
            ) {
                Text(
                    "Hello from Android",
                    modifier = Modifier.padding(top = 120.dp),
                )
            }
        }
    }
