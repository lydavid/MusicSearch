package ly.david.musicsearch.ui.common.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun materialYouColorScheme(
    darkTheme: Boolean,
): ColorScheme {
    val isAndroid12Plus = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    return when {
        isAndroid12Plus && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        isAndroid12Plus -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}
