package ly.david.musicsearch.shared.feature.nowplaying

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.nowPlayingHistory
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun NowPlayingHistoryUi(
    state: NowPlayingHistoryUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    NowPlayingHistoryUi(
        lazyPagingItems = state.lazyPagingItems,
        lazyListState = state.lazyListState,
        modifier = modifier,
        onBack = {
            eventSink(NowPlayingHistoryUiEvent.NavigateUp)
        },
        searchMusicBrainz = { query, entity ->
            eventSink(
                NowPlayingHistoryUiEvent.GoToSearch(
                    query = query,
                    entityType = entity,
                ),
            )
        },
        topAppBarFilterState = state.topAppBarFilterState,
        onDelete = { id ->
            eventSink(NowPlayingHistoryUiEvent.DeleteHistory(id))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NowPlayingHistoryUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = LazyListState(),
    topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    onBack: () -> Unit = {},
    searchMusicBrainz: (query: String, entity: MusicBrainzEntityType) -> Unit = { _, _ -> },
    onDelete: (String) -> Unit = {},
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = onBack,
                title = stringResource(Res.string.nowPlayingHistory),
                scrollBehavior = scrollBehavior,
                topAppBarFilterState = topAppBarFilterState,
            )
        },
    ) { innerPadding ->
        NowPlayingHistoryContent(
            lazyPagingItems = lazyPagingItems,
            lazyListState = lazyListState,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = searchMusicBrainz,
            onDelete = onDelete,
        )
    }
}

@Composable
private fun NowPlayingHistoryContent(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = LazyListState(),
    searchMusicBrainz: (query: String, entity: MusicBrainzEntityType) -> Unit = { _, _ -> },
    onDelete: (String) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            is NowPlayingHistoryListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        NowPlayingHistoryCard(
                            nowPlayingHistory = listItemModel,
                            onClick = {
                                searchMusicBrainz(
                                    "\"$title\" artist:\"$artist\"",
                                    MusicBrainzEntityType.RECORDING,
                                )
                            },
                        )
                    },
                    onDelete = {
                        onDelete(listItemModel.id)
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
