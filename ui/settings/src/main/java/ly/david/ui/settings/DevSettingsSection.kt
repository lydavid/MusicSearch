package ly.david.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.domain.Destination
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading

@Composable
internal fun DevSettingsSection(
    databaseVersion: Int = 0,
    onDestinationClick: (Destination) -> Unit = {},
) {
    Column {
        ListSeparatorHeader(text = "Dev Settings")

        TextWithHeading(heading = "Database version", text = "$databaseVersion")

        Text(
            modifier = Modifier
                .clickable {
                    onDestinationClick(Destination.EXPERIMENTAL_SPOTIFY)
                }
                .fillMaxWidth(),
            text = "Spotify"
        )
    }
}
