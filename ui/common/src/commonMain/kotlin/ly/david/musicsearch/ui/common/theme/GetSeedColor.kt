package ly.david.musicsearch.ui.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import ly.david.musicsearch.shared.domain.DEFAULT_SEED_COLOR_INT
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

@Composable
internal fun AppPreferences.getSeedColor(): Color {
    val accentSeedColor by observeSeedColor.collectAsState(initial = DEFAULT_SEED_COLOR_INT)
    return Color(accentSeedColor)
}
