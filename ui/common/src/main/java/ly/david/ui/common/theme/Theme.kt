package ly.david.ui.common.theme

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
//    primaryVariant = Orange700,
//    secondary = Purple200,
//    background = Color.Black,
//    surface = Color.Black,
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
//    primaryVariant = Orange700,
//    secondary = Purple200,
//    background = Color.White,
//    surface = Color.White,
    /* Other default colors to override
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

/**
 * Base theme for app, relying on user's device's color scheme.
 * Fallback to default color scheme for devices before Android 12.
 */
@Composable
fun BaseTheme(
    context: Context,
    darkTheme: Boolean = isSystemInDarkTheme(),
    materialYou: Boolean = true,
    content: @Composable () -> Unit
) {
    val isAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val useMaterialYou = materialYou && isAndroid12
    val colorSchemes = when {
        useMaterialYou && darkTheme -> dynamicDarkColorScheme(context)
        useMaterialYou -> dynamicLightColorScheme(context)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorSchemes,
//        typography = Typography,
//        shapes = Shapes,
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
//        typography = Typography,
//        shapes = Shapes,
        content = content
    )
}
