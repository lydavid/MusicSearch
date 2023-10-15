package ly.david.ui.nowplaying

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Instant
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.NowPlayingHistoryListItemModel
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NowPlayingHistoryScreen(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
    onDelete: (String) -> Unit = {},
) {
    PagingLoadingAndErrorHandler(
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
                        NowPlayingCard(
                            nowPlayingHistory = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                            onClick = {
                                searchMusicBrainz(
                                    "\"$title\" artist:\"$artist\"",
                                    ly.david.musicsearch.core.models.network.MusicBrainzEntity.RECORDING,
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
internal fun PreviewNowPlayingHistoryScreen() {
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
            NowPlayingHistoryScreen(lazyPagingItems = items.collectAsLazyPagingItems())
        }
    }
}
// endregion
