package ly.david.musicsearch.android.feature.spotify.history

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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Instant
import ly.david.musicsearch.android.feature.spotify.SpotifyUiEvent
import ly.david.musicsearch.android.feature.spotify.SpotifyUiState
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
        onDelete = { id ->
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
                title = strings.spotify,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpotifyHistoryContent(
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

            is SpotifyHistoryListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        SpotifyHistoryCard(
                            spotifyHistory = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                            onClick = {
                                // TODO: overlay to allow search artist/rg/recording?
                                searchMusicBrainz(
                                    "\"$trackName\" artist:\"$artistName\"",
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
                        SpotifyHistoryListItemModel(
                            id = "1di1C0QI6Y92yZPYn6XYAZ",
                            trackName = "シルエット",
                            artistName = "KANA-BOON",
                            albumName = "TIME",
                            lastListened = Instant.parse("2024-05-01T00:16:57.029Z"),
                        ),
                        SpotifyHistoryListItemModel(
                            id = "3hRRYgBeunE3PTmnzATTS0",
                            trackName = "天体観測",
                            artistName = "BUMP OF CHICKEN",
                            albumName = "jupiter",
                            lastListened = Instant.parse("2024-05-01T00:28:14.862Z"),
                        ),
                        ListSeparator(
                            id = "separator2",
                            text = "Friday, July 6, 2023",
                        ),
                        SpotifyHistoryListItemModel(
                            id = "0bA3KbwKjGktPb6wiglJ7Q",
                            trackName = "Hoshi Ni Naru",
                            artistName = "Islet",
                            albumName = "Hoshi Ni Naru",
                            lastListened = Instant.parse("2024-05-01T00:28:50.246Z"),
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
