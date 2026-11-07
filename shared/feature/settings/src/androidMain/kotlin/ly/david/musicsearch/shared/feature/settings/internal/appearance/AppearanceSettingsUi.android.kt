package ly.david.musicsearch.shared.feature.settings.internal.appearance

import android.os.Build
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@Composable
internal actual fun AppearanceSettingsUi(
    state: AppearanceSettingsUiState,
    modifier: Modifier,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    AppearanceSettingsUi(
        state = state,
        modifier = modifier,
        isAndroid12Plus = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S,
        isCompact = !windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND),
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
                isCompact = true,
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
                isCompact = true,
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
                isCompact = true,
            )
        }
    }
}
