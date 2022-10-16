package ly.david.mbjc.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ly.david.mbjc.R
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchScreenScaffold(
    openDrawer: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    searchQuery: String? = null,
    searchOption: MusicBrainzResource? = null,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ScrollableTopAppBar(
                title = stringResource(id = R.string.search_musicbrainz),
                openDrawer = openDrawer
            )
        },
    ) {
        SearchMusicBrainzScreen(
            snackbarHostState = snackbarHostState,
            lazyListState = lazyListState,
            onItemClick = onItemClick,
            searchQuery = searchQuery,
            searchOption = searchOption
        )
    }
}

// TODO: broken
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SearchScreenPreview() {
    PreviewTheme {
        SearchScreenScaffold()
    }
}
