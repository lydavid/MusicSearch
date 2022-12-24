package ly.david.mbjc.ui.settings.dev

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.mbjc.ui.settings.SettingSwitch

@Composable
internal fun DevSettingsSection(
    viewModel: DevSettingsViewModel = hiltViewModel()
) {

//    LaunchedEffect(key1 = Unit, block = {
//        viewModel.showThingFlow.first()
//    })

    Column {
        ListSeparatorHeader(text = "Dev Settings")

        // TODO: if it starts true, it will be colored correctly but its position is wrong
        val showThing by viewModel.showThingFlow.collectAsState(false)

        val showThingHeader = "Show thing"
        SettingSwitch(header = showThingHeader, checked = showThing, onCheckedChange = { viewModel.setShowThing(it) })
    }
}
