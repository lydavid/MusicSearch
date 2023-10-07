package ly.david.ui.nowplaying

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.musicsearch.domain.listitem.ListSeparator
import ly.david.musicsearch.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.R
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NowPlayingHistoryScaffold(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    NowPlayingHistoryScaffoldInternal(
        modifier = modifier,
        onBack = onBack,
        searchMusicBrainz = searchMusicBrainz,
    )
}

@Composable
internal fun NowPlayingHistoryScaffoldInternal(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
    viewModel: NowPlayingViewModel = koinViewModel(),
) {
    var filterText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.nowPlayingHistory)
        .collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()

    NowPlayingHistoryScaffoldInternal(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        onBack = onBack,
        searchMusicBrainz = searchMusicBrainz,
        filterText = filterText,
        onFilterTextChange = {
            filterText = it
            viewModel.updateQuery(query = it)
        },
        onDelete = { id ->
            scope.launch {
                viewModel.delete(id)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NowPlayingHistoryScaffoldInternal(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
    filterText: String = "",
    onFilterTextChange: (String) -> Unit = {},
    onDelete: (String) -> Unit = {},
) {
    val strings = LocalStrings.current
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = onBack,
                title = strings.nowPlayingHistory,
                scrollBehavior = scrollBehavior,
                filterText = filterText,
                onFilterTextChange = onFilterTextChange,
            )
        },
    ) { innerPadding ->
        NowPlayingHistoryScreen(
            lazyPagingItems = lazyPagingItems,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = searchMusicBrainz,
            onDelete = onDelete,
        )
    }
}

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewNowPlayingHistoryScaffold() {
    PreviewTheme {
        val items = MutableStateFlow(
            PagingData.from(
                listOf(
                    ListSeparator(
                        id = "separator1",
                        text = "Saturday, July 7, 2023"
                    ),
                    NowPlayingHistoryListItemModel(
                        id = "1",
                        title = "Title",
                        artist = "Artist",
                        lastPlayed = Instant.parse("2023-07-15T11:42:20Z"),
                    ),
                    NowPlayingHistoryListItemModel(
                        id = "2",
                        title = "Another Title",
                        artist = "A different artist",
                        lastPlayed = Instant.parse("2023-07-15T11:42:19Z"),
                    ),
                    ListSeparator(
                        id = "separator2",
                        text = "Friday, July 6, 2023"
                    ),
                    NowPlayingHistoryListItemModel(
                        id = "3",
                        title = "Yet Another Title",
                        artist = "A different artist",
                        lastPlayed = Instant.parse("2023-07-15T11:42:19Z"),
                    ),
                )
            )
        )
        NowPlayingHistoryScaffoldInternal(lazyPagingItems = items.collectAsLazyPagingItems())
    }
}
// endregion
