package ly.david.musicsearch.ui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

internal val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
)

internal val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
)

@Composable
internal expect fun appColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    materialYou: Boolean = true,
): ColorScheme
