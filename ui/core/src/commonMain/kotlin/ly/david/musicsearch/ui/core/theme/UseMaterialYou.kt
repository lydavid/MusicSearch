package ly.david.musicsearch.ui.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

@Composable
fun AppPreferences.useMaterialYou(): Boolean {
    val useMaterialYou by useMaterialYou.collectAsState(initial = false)
    return useMaterialYou
}
