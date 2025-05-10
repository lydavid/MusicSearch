package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader

@Composable
internal fun DevSettingsSection(
    isDeveloperMode: Boolean,
    onDeveloperModeChange: (Boolean) -> Unit,
) {
    Column {
        ListSeparatorHeader(text = "Developer settings")

        SettingSwitch(
            header = "Enable developer mode",
            checked = isDeveloperMode,
            onCheckedChange = onDeveloperModeChange,
        )
    }
}
