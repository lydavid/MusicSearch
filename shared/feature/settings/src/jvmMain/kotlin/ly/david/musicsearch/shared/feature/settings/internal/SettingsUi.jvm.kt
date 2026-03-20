package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun SettingsUi(
    state: SettingsUiState,
    modifier: Modifier,
) {
    SettingsUi(
        state = state,
        showAndroidSettings = false,
        modifier = modifier,
    )
}
