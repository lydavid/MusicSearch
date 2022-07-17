package ly.david.mbjc.ui.instrument

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
) {

    var titleState by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                title = titleState,
                onBack = onBack,
                dropdownMenuItems = {
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
        Text(text = "$instrumentId")
    }
}
