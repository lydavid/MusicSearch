package ly.david.musicsearch.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

@Composable
fun AppPreferences.useDarkTheme(): Boolean {
    val themePreference = theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
    return when (themePreference.value) {
        AppPreferences.Theme.LIGHT -> false
        AppPreferences.Theme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}
