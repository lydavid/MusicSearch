package ly.david.musicsearch.ui.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import ly.david.musicsearch.ui.core.theme.DarkColorScheme
import ly.david.musicsearch.ui.core.theme.LightColorScheme

@Composable
internal actual fun appColorScheme(
    darkTheme: Boolean,
    materialYou: Boolean,
): ColorScheme {
    return when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}
