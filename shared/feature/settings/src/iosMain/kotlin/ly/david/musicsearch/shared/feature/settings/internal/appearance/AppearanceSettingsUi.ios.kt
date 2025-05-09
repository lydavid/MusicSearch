package ly.david.musicsearch.shared.feature.settings.internal.appearance

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun AppearanceSettingsUi(
    state: AppearanceSettingsUiState,
    modifier: Modifier,
) {
    AppearanceSettingsUi(
        state = state,
        modifier = modifier,
        isAndroid12Plus = false,
    )
}
