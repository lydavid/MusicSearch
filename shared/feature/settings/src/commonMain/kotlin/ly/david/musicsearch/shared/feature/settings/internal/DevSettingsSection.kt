package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.developerSettings
import musicsearch.ui.common.generated.resources.enableDeveloperMode
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DevSettingsSection(
    isDeveloperMode: Boolean,
    onDeveloperModeChange: (Boolean) -> Unit,
) {
    Column {
        ListSeparatorHeader(text = stringResource(Res.string.developerSettings))

        SettingSwitch(
            header = stringResource(Res.string.enableDeveloperMode),
            checked = isDeveloperMode,
            onCheckedChange = onDeveloperModeChange,
        )
    }
}
