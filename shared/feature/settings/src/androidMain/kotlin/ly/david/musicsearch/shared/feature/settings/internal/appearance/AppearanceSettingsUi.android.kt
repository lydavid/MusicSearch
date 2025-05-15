package ly.david.musicsearch.shared.feature.settings.internal.appearance

import android.os.Build
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@Composable
internal actual fun AppearanceSettingsUi(
    state: AppearanceSettingsUiState,
    modifier: Modifier,
) {
    AppearanceSettingsUi(
        state = state,
        modifier = modifier,
        isAndroid12Plus = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
    )
}

@PreviewLightDark
@Composable
internal fun PreviewAppearanceSettingsUiCustomColors() {
    PreviewTheme {
        Surface {
            AppearanceSettingsUi(
                state = AppearanceSettingsUiState(
                    useMaterialYou = false,
                ),
                isAndroid12Plus = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAppearanceSettingsUiSystemColors() {
    PreviewTheme {
        Surface {
            AppearanceSettingsUi(
                state = AppearanceSettingsUiState(
                    useMaterialYou = true,
                ),
                isAndroid12Plus = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAppearanceSettingsUiNonAndroid12Plus() {
    PreviewTheme {
        Surface {
            AppearanceSettingsUi(
                state = AppearanceSettingsUiState(
                    useMaterialYou = false,
                ),
                isAndroid12Plus = false,
            )
        }
    }
}
