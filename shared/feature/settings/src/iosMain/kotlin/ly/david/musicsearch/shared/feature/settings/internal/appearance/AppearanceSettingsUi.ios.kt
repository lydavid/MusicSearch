package ly.david.musicsearch.shared.feature.settings.internal.appearance

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal actual fun AppearanceSettingsUi(
    state: AppearanceSettingsUiState,
    modifier: Modifier,
) {
    val windowSizeClass = calculateWindowSizeClass()
    AppearanceSettingsUi(
        state = state,
        modifier = modifier,
        isAndroid12Plus = false,
        isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
    )
}
