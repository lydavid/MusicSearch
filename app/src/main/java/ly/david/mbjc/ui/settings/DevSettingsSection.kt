package ly.david.mbjc.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import ly.david.mbjc.BuildConfig
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader

@Composable
fun DevSettingsSection() {
    Column {
        ListSeparatorHeader(text = "Dev Settings")

        // TODO: if it starts true, it will be colored correctly but its position is wrong
        //  This is a material3 bug that is fixed by updating it
        //  But that update will break our app, so let's wait for stable release to update/fix
        //  For now we can use non-material3 switch
//        val showThing by viewModel.showThingFlow.collectAsState(false)
//
//        val showThingHeader = "Show thing"
//        SettingSwitch(
//            header = showThingHeader,
//            checked = showThing,
//            onCheckedChange = { checked ->
//                viewModel.setShowThing(checked)
//            })

        // Bump this by searching for `versionCode`
        TextWithHeading(heading = "Internal version", text = BuildConfig.VERSION_CODE.toString())
    }
}
