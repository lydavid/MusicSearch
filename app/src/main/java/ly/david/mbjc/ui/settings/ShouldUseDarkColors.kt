package ly.david.mbjc.ui.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun AppPreferences.shouldUseDarkColors(): Boolean {
    val themePreference = theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
    return when (themePreference.value) {
        AppPreferences.Theme.LIGHT -> false
        AppPreferences.Theme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}
