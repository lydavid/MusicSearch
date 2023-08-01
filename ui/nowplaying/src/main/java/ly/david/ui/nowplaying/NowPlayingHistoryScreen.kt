package ly.david.ui.nowplaying

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.data.common.toDate
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.ListSeparator
import ly.david.data.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.data.network.MusicBrainzEntity
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
                                    MusicBrainzEntity.RECORDING,
                                )
                            },
                        )
                    },
                    onDelete = {
                        onDelete(listItemModel.id)
                    }
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
                            text = "Saturday, July 7, 2023"
                        ),
                        NowPlayingHistoryListItemModel(
                            id = "1",
                            title = "Title",
                            artist = "Artist",
                            lastPlayed = "2023-07-15 11:42:20".toDate(),
                        ),
                        NowPlayingHistoryListItemModel(
                            id = "2",
                            title = "Another Title",
                            artist = "A different artist",
                            lastPlayed = "2023-07-15 11:42:19".toDate(),
                        ),
                        ListSeparator(
                            id = "separator2",
                            text = "Friday, July 6, 2023"
                        ),
                        NowPlayingHistoryListItemModel(
                            id = "3",
                            title = "Yet Another Title",
                            artist = "A different artist",
                            lastPlayed = "2023-07-15 11:42:19".toDate(),
                        ),
                    )
                )
            )
            NowPlayingHistoryScreen(lazyPagingItems = items.collectAsLazyPagingItems())
        }
    }
}
// endregion
