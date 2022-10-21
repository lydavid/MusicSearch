package ly.david.mbjc.ui.event

import androidx.annotation.StringRes
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
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar

private enum class EventTab(@StringRes val titleRes: Int) {
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventScaffold(
    eventId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {

    val resource = MusicBrainzResource.EVENT

    var titleState by rememberSaveable { mutableStateOf("") }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        titleState = titleWithDisambiguation
    }

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = resource,
                title = titleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource, eventId)
                    CopyToClipboardMenuItem(eventId)
                },
            )
        },
    ) { innerPadding ->
        EventScreen(
            modifier = Modifier.padding(innerPadding),
            eventId = eventId,
            onTitleUpdate = { title ->
                if (titleWithDisambiguation.isNullOrEmpty()) {
                    titleState = title
                }
            },
            onItemClick = onItemClick
        )
    }
}
