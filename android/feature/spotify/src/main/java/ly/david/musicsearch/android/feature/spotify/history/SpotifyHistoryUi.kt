package ly.david.musicsearch.android.feature.spotify.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.common.getDateFormatted
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.core.LocalStrings
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun SpotifyHistoryUi(
    state: SpotifyUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    SpotifyHistoryUi(
        lazyPagingItems = state.lazyPagingItems,
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
        onDelete = {
            // TODO: support deleting spotify history
//            eventSink(NowPlayingHistoryUiEvent.DeleteHistory(id))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpotifyHistoryUi(
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
                title = strings.spotifyHistory,
                scrollBehavior = scrollBehavior,
                filterText = filterText,
                onFilterTextChange = onFilterTextChange,
            )
        },
    ) { innerPadding ->
        SpotifyHistoryContent(
            lazyPagingItems = lazyPagingItems,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = searchMusicBrainz,
            onDelete = onDelete,
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
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
    onDelete: (String) -> Unit = {},
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
                            text = Instant.parse("2024-05-01T02:29:38.973Z").getDateFormatted(),
                        ),
                        SpotifyHistoryListItemModel(
                            id = "spotify:track:2ritsV4U3jq2LduJpovZ1A${Instant.parse("2024-05-01T02:29:38.973Z")}",
                            trackName = "だれかの心臓になれたなら",
                            artistName = "Yurry Canon",
                            albumName = "Kardia",
                            trackLengthMilliseconds = 231724,
                            lastListened = Instant.parse("2024-05-01T02:29:38.973Z"),
                        ),
                        SpotifyHistoryListItemModel(
                            id = "3hRRYgBeunE3PTmnzATTS0${Instant.parse("2024-05-01T02:29:38.973Z")}",
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
