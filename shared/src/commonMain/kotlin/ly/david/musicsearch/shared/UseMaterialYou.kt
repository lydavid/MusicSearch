package ly.david.musicsearch.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import ly.david.musicsearch.core.preferences.AppPreferences

@Composable
fun AppPreferences.useMaterialYou(): Boolean {
    val useMaterialYou = useMaterialYou.collectAsState(initial = true)
    return useMaterialYou.value
}
