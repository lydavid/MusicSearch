package ly.david.mbjc.ui.experimental

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExperimentalScaffold(
    openDrawer: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                openDrawer = openDrawer,
                title = "Experimental",
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(
                modifier = Modifier
                    .clickable {
                        onItemClick(Destination.EXPERIMENTAL_SETTINGS, "", null)
                    }
                    .fillMaxWidth(),
                text = "Settings"
            )

            Text(
                modifier = Modifier
                    .clickable {
                        onItemClick(Destination.EXPERIMENTAL_SPOTIFY, "", null)
                    }
                    .fillMaxWidth(),
                text = "Spotify"
            )
        }
    }
}
