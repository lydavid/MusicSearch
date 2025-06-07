package ly.david.musicsearch.ui.common.theme

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

/**
 * Non-Android code won't ever be called.
 */
@Composable
internal expect fun materialYouColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
): ColorScheme
