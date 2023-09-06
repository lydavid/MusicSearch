import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val windowState = rememberWindowState()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "MusicSearch"
    ) {
        Row {
            Text("Hello world!")
        }
    }
}
