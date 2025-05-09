package ly.david.musicsearch.ui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.materialkolor.rememberDynamicColorScheme
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

@Composable
fun BaseTheme(
    appPreferences: AppPreferences,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    val materialYou = appPreferences.useMaterialYou()
    val colorScheme = if (materialYou) {
        materialYouColorScheme(
            darkTheme = darkTheme,
        )
    } else {
        val seedColor = appPreferences.getSeedColor()
        val seededColorScheme = rememberDynamicColorScheme(
            seedColor = seedColor,
            isDark = darkTheme,
        )
        seededColorScheme
    }
    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}
