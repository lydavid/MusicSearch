package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewSettingSwitchChecked() {
    PreviewTheme {
        Surface {
            SettingSwitch(
                header = "A setting",
                checked = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingSwitchUnchecked() {
    PreviewTheme {
        Surface {
            SettingSwitch(
                header = "A setting",
                checked = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSettingSwitchWithSubtitle() {
    PreviewTheme {
        Surface {
            SettingSwitch(
                header = "A setting",
                subtitle = "With some subtitle",
                checked = true,
            )
        }
    }
}
