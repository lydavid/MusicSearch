package ly.david.musicsearch.shared.feature.nowplaying

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.nowPlayingHistory
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NowPlayingHistoryUi(
    state: NowPlayingHistoryUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = state.scrollToHideTopAppBar,
        snackbarHostState = SnackbarHostState(),
        topBar = { scrollBehavior ->
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = { eventSink(NowPlayingHistoryUiEvent.NavigateUp) },
                title = stringResource(Res.string.nowPlayingHistory),
                scrollBehavior = scrollBehavior,
                topAppBarFilterState = state.topAppBarFilterState,
            )
        },
    ) { innerPadding, scrollBehavior ->
        NowPlayingHistoryContent(
            lazyPagingItems = state.lazyPagingItems,
            filterText = state.topAppBarFilterState.filterText,
            lazyListState = state.lazyListState,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = { query, entity ->
                eventSink(
                    NowPlayingHistoryUiEvent.GoToSearch(
                        query = query,
                        entityType = entity,
                    ),
                )
            },
            onDelete = { id ->
                eventSink(NowPlayingHistoryUiEvent.DeleteHistory(id))
            },
        )
    }
}

@Composable
private fun NowPlayingHistoryContent(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    filterText: String,
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
                        NowPlayingHistoryListItem(
                            nowPlayingHistory = listItemModel,
                            filterText = filterText,
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
