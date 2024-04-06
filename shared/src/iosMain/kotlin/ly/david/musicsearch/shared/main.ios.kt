package ly.david.musicsearch.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import ly.david.ui.core.theme.BaseTheme
import platform.UIKit.UIViewController

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController =
    ComposeUIViewController {
        BaseTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    "Hello from Android",
                    modifier = Modifier.padding(top = 120.dp),
                )
            }
        }
    }
