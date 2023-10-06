
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ly.david.ui.core.theme.BaseTheme
import ly.david.ui.core.theme.TextStyles

fun main() = application {
    val windowState = rememberWindowState()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "MusicSearch"
    ) {
        BaseTheme(
            content = {
                Card(
                    modifier = Modifier.padding(16.dp),
                    backgroundColor = MaterialTheme.colors.onBackground,
                ) {
                    Text(
                        "Hello world!",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyles.getHeaderTextStyle(),
                        color = MaterialTheme.colors.primary,
                    )
                }
            }
        )
    }
}
