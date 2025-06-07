package ly.david.musicsearch.ui.common.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun materialYouColorScheme(
    darkTheme: Boolean,
): ColorScheme {
    return when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}
