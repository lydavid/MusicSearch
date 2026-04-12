package ly.david.musicsearch.shared.feature.spotify.history

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.spotifyHistory
import org.jetbrains.compose.resources.stringResource
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("SwallowedException")
@Composable
internal fun SpotifyHistoryUi(
    state: SpotifyUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = state.scrollToHideTopAppBar,
        snackbarHostState = snackbarHostState,
        topBar = { scrollBehavior ->
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = { eventSink(SpotifyUiEvent.NavigateUp) },
                title = stringResource(Res.string.spotifyHistory),
                scrollBehavior = scrollBehavior,
                topAppBarFilterState = state.topAppBarFilterState,
            )
        },
    ) { innerPadding, scrollBehavior ->
        SpotifyHistoryContent(
            lazyPagingItems = state.lazyPagingItems,
            filterText = state.topAppBarFilterState.filterText,
            lazyListState = state.lazyListState,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = { query, entity ->
                eventSink(
                    SpotifyUiEvent.GoToSearch(
                        query = query,
                        entityType = entity,
                    ),
                )
            },
            onDelete = { history ->
                scope.launch {
                    eventSink(SpotifyUiEvent.MarkForDeletion(history))

                    try {
                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = "Removed ${history.trackName}",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short,
                        )

                        when (snackbarResult) {
                            SnackbarResult.ActionPerformed -> {
                                eventSink(SpotifyUiEvent.UndoMarkForDeletion(history))
                            }

                            SnackbarResult.Dismissed -> {
                                eventSink(SpotifyUiEvent.Delete(history))
                            }
                        }
                    } catch (_: CancellationException) {
                        eventSink(SpotifyUiEvent.Delete(history))
                    }
                }
            },
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
private fun SpotifyHistoryContent(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    filterText: String,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = LazyListState(),
    searchMusicBrainz: (query: String, entity: MusicBrainzEntityType) -> Unit = { _, _ -> },
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
        modifier = modifier,
        lazyListState = lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            is SpotifyHistoryListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        SpotifyHistoryListItem(
                            spotifyHistory = listItemModel,
                            filterText = filterText,
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
