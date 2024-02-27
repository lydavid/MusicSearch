package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun Settings(
    state: SettingsUiState,
    modifier: Modifier,
) {
    Settings(
        state = state,
        showAndroidSettings = false,
        modifier = modifier,
    )
}
