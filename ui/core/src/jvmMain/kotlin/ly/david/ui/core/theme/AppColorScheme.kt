package ly.david.ui.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun AppColorScheme(
    darkTheme: Boolean,
    materialYou: Boolean,
): ColorScheme {
    return when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}
