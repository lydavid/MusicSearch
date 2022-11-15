package ly.david.mbjc.ui.instrument

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
internal fun InstrumentScaffold(
    instrumentId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {

    var titleState by rememberSaveable { mutableStateOf("") }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        titleState = titleWithDisambiguation
    }

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = MusicBrainzResource.INSTRUMENT,
                title = titleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.INSTRUMENT, resourceId = instrumentId)
                },
            )
        },
    ) { innerPadding ->
        InstrumentScreen(
            modifier = Modifier.padding(innerPadding),
            instrumentId = instrumentId,
            onTitleUpdate = { title ->
                if (titleWithDisambiguation.isNullOrEmpty()) {
                    titleState = title
                }
            },
            onItemClick = onItemClick
        )
    }
}
