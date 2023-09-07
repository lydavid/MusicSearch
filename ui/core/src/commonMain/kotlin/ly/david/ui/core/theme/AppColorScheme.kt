package ly.david.ui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal expect fun AppColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    materialYou: Boolean = true,
): ColorScheme
