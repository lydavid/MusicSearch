package ly.david.mbjc.ui.collections

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * Presents a list of all of your collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionListScaffold(
    modifier: Modifier = Modifier,
    onDestinationClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
//    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                showBackButton = false,
                title = stringResource(id = R.string.collections),
            )
        },
    ) { innerPadding ->

        LazyColumn(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()) {

            items(MusicBrainzResource.values()) {
                CollectionGroup(musicBrainzResource = it)
            }
        }
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            CollectionListScaffold()
        }
    }
}
