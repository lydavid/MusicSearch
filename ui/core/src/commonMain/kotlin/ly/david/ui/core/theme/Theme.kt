package ly.david.ui.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
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
fun BaseTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = AppColorScheme(),
        content = content
    )
}

/**
 * Theme for previews/tests and devices below Android 12.
 */
@Composable
fun PreviewTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorSchemes = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorSchemes,
        content = content
    )
}
