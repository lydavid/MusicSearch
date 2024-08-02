package ly.david.musicsearch.ui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun BaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    materialYou: Boolean = true,
    content: @Composable () -> Unit,
) {
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = appColorScheme(
                darkTheme = darkTheme,
                materialYou = materialYou,
            ),
            content = content,
        )
    }
}
