package ly.david.mbjc.ui.place

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.navigation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlaceScaffold(
    placeId: String,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {

    var titleState by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = MusicBrainzResource.PLACE,
                title = titleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.PLACE, resourceId = placeId)
                },
            )
        },
    ) { innerPadding ->
        PlaceScreen(
            modifier = Modifier.padding(innerPadding),
            context = context,
            placeId = placeId,
            onTitleUpdate = { title ->
                titleState = title
            },
            onItemClick = onItemClick
        )
    }
}
