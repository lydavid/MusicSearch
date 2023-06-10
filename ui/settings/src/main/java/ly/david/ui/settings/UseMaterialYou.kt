package ly.david.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun AppPreferences.useMaterialYou(): Boolean {
    val useMaterialYou = useMaterialYou.collectAsState(initial = true)
    return useMaterialYou.value
}
