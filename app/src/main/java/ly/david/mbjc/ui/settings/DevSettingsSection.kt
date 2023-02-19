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

        // Bump this by searching for `versionCode`
        TextWithHeading(heading = "Internal version", text = BuildConfig.VERSION_CODE.toString())
    }
}
