package ly.david.musicsearch.android.feature.nowplaying.internal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun NowPlayingHistoryUi(
    state: NowPlayingHistoryUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    NowPlayingHistoryUi(
        lazyPagingItems = state.lazyPagingItems,
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
        filterText = state.query,
        onFilterTextChange = {
            eventSink(NowPlayingHistoryUiEvent.UpdateQuery(it))
        },
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
        NowPlayingHistoryContent(
            lazyPagingItems = lazyPagingItems,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = searchMusicBrainz,
            onDelete = onDelete,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NowPlayingHistoryContent(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
    onDelete: (String) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
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
                            modifier = Modifier.animateItemPlacement(),
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
@DefaultPreviews
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
