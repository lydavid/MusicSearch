package ly.david.musicsearch.ui.common.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import ly.david.musicsearch.ui.common.theme.DarkColorScheme
import ly.david.musicsearch.ui.common.theme.DarkExtendedColors
import ly.david.musicsearch.ui.common.theme.LightColorScheme
import ly.david.musicsearch.ui.common.theme.LightExtendedColors
import ly.david.musicsearch.ui.common.theme.LocalExtendedColors

/**
 * Theme for previews/tests.
 */
@Composable
fun PreviewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorSchemes = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorSchemes,
            content = content,
        )
    }
}
