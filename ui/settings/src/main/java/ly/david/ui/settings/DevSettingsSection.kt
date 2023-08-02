package ly.david.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading

@Composable
internal fun DevSettingsSection(
    databaseVersion: Int = 0,
) {
    Column {
        ListSeparatorHeader(text = "Dev Settings")

        TextWithHeading(heading = "Database version", text = "$databaseVersion")
    }
}
