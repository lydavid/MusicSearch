package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
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

@DefaultPreviews
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
