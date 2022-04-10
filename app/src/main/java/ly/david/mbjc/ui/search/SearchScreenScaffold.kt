package ly.david.mbjc.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import ly.david.mbjc.ui.Destination
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreenScaffold(
    openDrawer: () -> Unit = {},
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> }
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { ScrollableTopAppBar(title = "Search Music Brainz", openDrawer = openDrawer) },
    ) {
        SearchMusicBrainzScreen(
            snackbarHostState = snackbarHostState,
            lazyListState = lazyListState,
            onItemClick = onItemClick,
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SearchScreenPreview() {
    MusicBrainzJetpackComposeTheme {
        SearchScreenScaffold()
    }
}
