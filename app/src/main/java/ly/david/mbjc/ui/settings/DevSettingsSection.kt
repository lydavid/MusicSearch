package ly.david.mbjc.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.mbjc.ui.settings.components.SettingSwitch

@Composable
fun DevSettingsSection(
    viewModel: SettingsViewModel
) {
    Column {
        ListSeparatorHeader(text = "Dev Settings")

        // TODO: if it starts true, it will be colored correctly but its position is wrong
        //  This is a material3 bug that is fixed by updating it
        //  But that update will break our app, so let's wait for stable release to update/fix
        //  For now we can use non-material3 switch
        val showThing by viewModel.showThingFlow.collectAsState(false)

        val showThingHeader = "Show thing"
        SettingSwitch(
            header = showThingHeader,
            checked = showThing,
            onCheckedChange = { checked ->
                viewModel.setShowThing(checked)
            })
    }
}
