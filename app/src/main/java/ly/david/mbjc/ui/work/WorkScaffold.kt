package ly.david.mbjc.ui.work

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.navigation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WorkScaffold(
    workId: String,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {

    var titleState by rememberSaveable { mutableStateOf("") }
    var subtitleState by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = MusicBrainzResource.WORK,
                title = titleState,
                subtitle = subtitleState,
                onBack = onBack,
                dropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text("Open in browser") },
                        onClick = {
                            context.lookupInBrowser(MusicBrainzResource.WORK, workId)
                            closeMenu()
                        }
                    )
                },
            )
        },
    ) { innerPadding ->
        WorkScreen(
            modifier = Modifier.padding(innerPadding),
            workId = workId,
            onTitleUpdate = { title, subtitle ->
                titleState = title
                subtitleState = subtitle
            },
            onItemClick = onItemClick
        )
    }
}
