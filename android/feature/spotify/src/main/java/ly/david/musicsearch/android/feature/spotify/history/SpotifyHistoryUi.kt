package ly.david.musicsearch.android.feature.spotify.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.common.getDateFormatted
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@Composable
internal fun SpotifyHistoryUi(
    state: SpotifyUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    SpotifyHistoryUi(
        lazyPagingItems = state.lazyPagingItems,
        lazyListState = state.lazyListState,
        modifier = modifier,
        onBack = {
            eventSink(SpotifyUiEvent.NavigateUp)
        },
        searchMusicBrainz = { query, entity ->
            eventSink(
                SpotifyUiEvent.GoToSearch(
                    query = query,
                    entity = entity,
                ),
            )
        },
        filterText = state.query,
        onFilterTextChange = {
            eventSink(SpotifyUiEvent.UpdateQuery(it))
        },
        onMarkAsDeleted = {
            eventSink(SpotifyUiEvent.MarkAsDeleted(it))
        },
        onUndoMarkAsDeleted = {
            eventSink(SpotifyUiEvent.UndoMarkAsDeleted(it))
        },
        onDelete = {
            eventSink(SpotifyUiEvent.Delete(it))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpotifyHistoryUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = LazyListState(),
    onBack: () -> Unit = {},
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
    filterText: String = "",
    onFilterTextChange: (String) -> Unit = {},
    onMarkAsDeleted: (SpotifyHistoryListItemModel) -> Unit = {},
    onUndoMarkAsDeleted: (SpotifyHistoryListItemModel) -> Unit = {},
    onDelete: (SpotifyHistoryListItemModel) -> Unit = {},
) {
    val strings = LocalStrings.current
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = onBack,
                title = strings.spotifyHistory,
                scrollBehavior = scrollBehavior,
                filterText = filterText,
                onFilterTextChange = onFilterTextChange,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SwipeToDismissBox(
                    state = rememberSwipeToDismissBoxState(),
                    backgroundContent = {},
                    content = { Snackbar(snackbarData) },
                )
            }
        },
    ) { innerPadding ->
        SpotifyHistoryContent(
            lazyPagingItems = lazyPagingItems,
            lazyListState = lazyListState,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = searchMusicBrainz,
            onDelete = { history ->
                scope.launch {
                    onMarkAsDeleted(history)

                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = "Removed ${history.trackName}",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short,
                    )

                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            onUndoMarkAsDeleted(history)
                        }

                        SnackbarResult.Dismissed -> {
                            onDelete(history)
                        }
                    }
                }
            },
        )
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
private fun SpotifyHistoryContent(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = LazyListState(),
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
    onDelete: (SpotifyHistoryListItemModel) -> Unit = {},
) {
    var clickListItem: SpotifyHistoryListItemModel? by rememberSaveable { mutableStateOf(null) }

    if (clickListItem != null) {
        ModalBottomSheet(
            onDismissRequest = { clickListItem = null },
        ) {
            SearchSpotifyBottomSheetContent(
                spotifyHistory = clickListItem,
                searchMusicBrainz = { query, entity ->
                    searchMusicBrainz(
                        query,
                        entity,
                    )
                    clickListItem = null
                },
            )
        }
    }

    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
        modifier = modifier,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            is SpotifyHistoryListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        SpotifyHistoryCard(
                            spotifyHistory = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                            onClick = {
                                clickListItem = this
                            },
                        )
                    },
                    onDelete = {
                        onDelete(listItemModel)
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
                            text = Instant.parse("2024-05-01T02:29:38.973Z").getDateFormatted(),
                        ),
                        SpotifyHistoryListItemModel(
                            id = "spotify:track:2ritsV4U3jq2LduJpovZ1A${Instant.parse("2024-05-01T02:29:38.973Z")}",
                            trackId = "spotify:track:2ritsV4U3jq2LduJpovZ1A",
                            trackName = "だれかの心臓になれたなら",
                            artistName = "Yurry Canon",
                            albumName = "Kardia",
                            trackLengthMilliseconds = 231724,
                            lastListened = Instant.parse("2024-05-01T02:29:38.973Z"),
                        ),
                        SpotifyHistoryListItemModel(
                            id = "spotify:track:3hRRYgBeunE3PTmnzATTS0${Instant.parse("2024-05-01T02:29:38.973Z")}",
                            trackId = "spotify:track:3hRRYgBeunE3PTmnzATTS0",
                            trackName = "天体観測",
                            artistName = "BUMP OF CHICKEN",
                            albumName = "jupiter",
                            lastListened = Instant.parse("2024-05-01T02:29:38.973Z"),
                        ),
                        ListSeparator(
                            id = "separator2",
                            text = Instant.parse("2024-04-30T02:29:38.973Z").getDateFormatted(),
                        ),
                        SpotifyHistoryListItemModel(
                            id = "spotify:track:2ritsV4U3jq2LduJpovZ1A${Instant.parse("2024-04-30T02:29:38.973Z")}",
                            trackId = "spotify:track:2ritsV4U3jq2LduJpovZ1A",
                            trackName = "だれかの心臓になれたなら",
                            artistName = "Yurry Canon",
                            albumName = "Kardia",
                            trackLengthMilliseconds = 231724,
                            lastListened = Instant.parse("2024-04-30T02:29:38.973Z"),
                        ),
                    ),
                ),
            )
            SpotifyHistoryUi(
                lazyPagingItems = items.collectAsLazyPagingItems(),
            )
        }
    }
}
// endregion
