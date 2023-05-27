package ly.david.mbjc.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.navigation.Destination
import ly.david.data.persistence.DATABASE_VERSION
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading

@Composable
fun DevSettingsSection(
    onDestinationClick: (Destination) -> Unit = {},
) {
    Column {
        ListSeparatorHeader(text = "Dev Settings")

        TextWithHeading(heading = "Database version", text = "$DATABASE_VERSION")

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
