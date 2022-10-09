package ly.david.mbjc.ui.instrument

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
internal fun InstrumentScaffold(
    instrumentId: String,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {

    var titleState by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = MusicBrainzResource.INSTRUMENT,
                title = titleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text("Open in browser") },
                        onClick = {
                            context.lookupInBrowser(MusicBrainzResource.INSTRUMENT, instrumentId)
                            closeMenu()
                        }
                    )
                },
            )
        },
    ) { innerPadding ->
        InstrumentScreen(
            modifier = Modifier.padding(innerPadding),
            instrumentId = instrumentId,
            onTitleUpdate = { title ->
                titleState = title
            },
            onItemClick = onItemClick
        )
    }
}
