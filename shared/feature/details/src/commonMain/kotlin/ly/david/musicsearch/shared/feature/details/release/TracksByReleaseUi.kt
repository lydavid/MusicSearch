package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.listitem.CollapsibleListSeparator
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.track.TrackListItem

/**
 * Shows all tracks in all media in this release.
 *
 * Tracks are recordings that are part of a release. A track references a recording,
 * but some of its details (e.g. name) might be different for a given release.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TracksByReleaseUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    onRecordingClick: (id: String, title: String) -> Unit = { _, _ -> },
    onToggleMedium: (id: String) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is TrackListItemModel -> {
                TrackListItem(
                    track = listItemModel,
                    onRecordingClick = onRecordingClick,
                )
            }

            is CollapsibleListSeparator -> {
                CollapsibleListSeparatorHeader(
                    text = listItemModel.text,
                    collapsed = listItemModel.collapsed,
                    onClick = { onToggleMedium(listItemModel.id) },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
