package ly.david.musicsearch.ui.core.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun appColorScheme(
    darkTheme: Boolean,
    materialYou: Boolean,
): ColorScheme {
    val isAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val useMaterialYou = materialYou && isAndroid12
    return when {
        useMaterialYou && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        useMaterialYou -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
}
