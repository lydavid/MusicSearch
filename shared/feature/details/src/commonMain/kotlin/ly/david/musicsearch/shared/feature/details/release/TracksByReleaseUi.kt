package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.track.TrackListItem
import ly.david.musicsearch.ui.common.track.TracksByReleaseUiEvent
import ly.david.musicsearch.ui.common.track.TracksByReleaseUiState

@Composable
internal fun TracksByReleaseUi(
    uiState: TracksByReleaseUiState,
    modifier: Modifier = Modifier,
    onRecordingClick: (id: String) -> Unit = {},
) {
    val eventSink = uiState.eventSink

    TracksByReleaseUi(
        lazyPagingItems = uiState.pagingDataFlow.collectAsLazyPagingItems(),
        mostListenedTrackCount = uiState.mostListenedTrackCount,
        modifier = modifier,
        lazyListState = uiState.lazyListState,
        collapsedMediumIds = uiState.collapsedMediumIds,
        onRecordingClick = onRecordingClick,
        onToggleMedium = { id ->
            eventSink(TracksByReleaseUiEvent.ToggleMedium(id))
        },
    )
}

/**
 * Shows all tracks in all media in this release.
 *
 * Tracks are recordings that are part of a release. A track references a recording,
 * but some of its details (e.g. name) might be different for a given release.
 */
@Composable
internal fun TracksByReleaseUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    mostListenedTrackCount: Long,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    collapsedMediumIds: Set<Long> = setOf(),
    onRecordingClick: (id: String) -> Unit = {},
    onToggleMedium: (id: String) -> Unit = {},
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        lazyListState = lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is TrackListItemModel -> {
                if (!collapsedMediumIds.contains(listItemModel.mediumId)) {
                    TrackListItem(
                        track = listItemModel,
                        mostListenedTrackCount = mostListenedTrackCount,
                        onRecordingClick = onRecordingClick,
                    )
                }
            }

            is ListSeparator -> {
                CollapsibleListSeparatorHeader(
                    text = listItemModel.text,
                    collapsed = collapsedMediumIds.contains(listItemModel.id.toLong()),
                    onClick = { onToggleMedium(listItemModel.id) },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
