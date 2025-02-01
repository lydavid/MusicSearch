package ly.david.musicsearch.android.feature.nowplaying.internal

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.PreviewTheme

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
                    entity = entity,
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
private fun NowPlayingHistoryUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = LazyListState(),
    topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    onBack: () -> Unit = {},
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
    onDelete: (String) -> Unit = {},
) {
    val strings = LocalStrings.current
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = onBack,
                title = strings.nowPlayingHistory,
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
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
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
                                    MusicBrainzEntity.RECORDING,
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

// region Previews
@PreviewLightDark
@Composable
internal fun PreviewNowPlayingHistoryUi() {
    PreviewTheme {
        Surface {
            val items = MutableStateFlow(
                PagingData.from(
                    listOf(
                        ListSeparator(
                            id = "separator1",
                            text = "Saturday, July 7, 2023",
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
                            text = "Friday, July 6, 2023",
                        ),
                        NowPlayingHistoryListItemModel(
                            id = "3",
                            title = "Yet Another Title",
                            artist = "A different artist",
                            lastPlayed = Instant.parse("2023-07-15T11:42:19Z"),
                        ),
                    ),
                ),
            )
            NowPlayingHistoryUi(
                lazyPagingItems = items.collectAsLazyPagingItems(),
            )
        }
    }
}
// endregion
