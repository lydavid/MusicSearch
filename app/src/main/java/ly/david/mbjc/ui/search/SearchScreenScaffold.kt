package ly.david.mbjc.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.domain.UiData
import ly.david.mbjc.ui.Destination
import ly.david.mbjc.ui.common.ScrollableTopAppBar
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
internal fun SearchScreenScaffold(
    openDrawer: () -> Unit = {},
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: SearchMusicBrainzViewModel = hiltViewModel()
) {

    val lazyListState: LazyListState = rememberLazyListState()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    // TODO: move down if we don't generalize this screen
    val lazyPagingItems: LazyPagingItems<UiData> = viewModel.searchResultsUiData.collectAsLazyPagingItems()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { ScrollableTopAppBar(title = "Search Music Brainz", openDrawer = openDrawer) },
    ) {
        SearchMusicBrainzScreen(
            lazyListState = lazyListState,
            scaffoldState = scaffoldState,
            lazyPagingItems = lazyPagingItems,
            onSearch = { resource, query ->
                viewModel.updateViewModelState(resource, query)
            },
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
