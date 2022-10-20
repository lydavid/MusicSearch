package ly.david.mbjc.ui.recording

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordingScaffold(
    recordingId: String,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {

    var titleState by rememberSaveable { mutableStateOf("") }
    var subtitleState by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = MusicBrainzResource.RECORDING,
                title = titleState,
                subtitle = subtitleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.RECORDING, resourceId = recordingId)
                },
            )
        },
    ) { innerPadding ->
        RecordingScreen(
            modifier = Modifier.padding(innerPadding),
            recordingId = recordingId,
            onTitleUpdate = { title, subtitle ->
                titleState = title
                subtitleState = subtitle
            },
            onItemClick = onItemClick
        )
    }
}
