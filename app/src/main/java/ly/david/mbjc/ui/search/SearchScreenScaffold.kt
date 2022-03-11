package ly.david.mbjc.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.ui.common.ScrollableTopAppBar
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
internal fun SearchScreenScaffold(
    openDrawer: () -> Unit = {},
    onArtistClick: (String) -> Unit = {},
    viewModel: SearchArtistsViewModel = viewModel()
) {

    val lazyListState: LazyListState = rememberLazyListState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    // TODO: move down if we don't generalize this screen
    val lazyPagingItems: LazyPagingItems<SearchArtistsUiModel> = viewModel.artists.collectAsLazyPagingItems()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { ScrollableTopAppBar(title = "Search Artists", openDrawer = openDrawer) },
    ) {
        SearchArtistsScreen(
            lazyListState = lazyListState,
            scaffoldState = scaffoldState,
            lazyPagingItems = lazyPagingItems,
            onSearch = { query ->
                viewModel.query.value = query
            },
            onArtistClick = onArtistClick,
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
