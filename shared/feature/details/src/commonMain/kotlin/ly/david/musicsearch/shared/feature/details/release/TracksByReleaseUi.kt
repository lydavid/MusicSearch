package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.SelectableId
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.paging.createFilteredCountHeader
import ly.david.musicsearch.ui.common.track.TrackListItem
import ly.david.musicsearch.ui.common.track.TracksByReleaseUiEvent
import ly.david.musicsearch.ui.common.track.TracksByReleaseUiState

/**
 * Shows all tracks in all media in this release.
 *
 * Tracks are recordings that are part of a release. A track references a recording,
 * but some of its details (e.g. name) might be different for a given release.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TracksByReleaseUi(
    uiState: TracksByReleaseUiState,
    filterText: String,
    modifier: Modifier = Modifier,
    onRecordingClick: (id: String) -> Unit = {},
    selectedIds: ImmutableList<SelectableId> = persistentListOf(),
    onSelect: (SelectableId) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
    onSubmitListenClick: (track: TrackListItemModel) -> Unit = {},
) {
    val eventSink = uiState.eventSink

    var showBottomSheetForTrack: TrackListItemModel? by remember { mutableStateOf(null) }
    showBottomSheetForTrack?.let { track ->
        ModalBottomSheet(
            onDismissRequest = { showBottomSheetForTrack = null },
        ) {
            TrackAdditionalActionsBottomSheetContent(
                track = track,
                onDismiss = { showBottomSheetForTrack = null },
                onAddToCollectionClick = onEditCollectionClick,
                onSubmitListenClick = onSubmitListenClick,
            )
        }
    }

    val header = uiState.createFilteredCountHeader()

    ScreenWithPagingLoadingAndError(
        lazyPagingItems = uiState.pagingDataFlow.collectAsLazyPagingItems(),
        modifier = modifier,
        lazyListState = uiState.lazyListState,
        header = header,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is TrackListItemModel -> {
                if (!uiState.collapsedMediumIds.contains(listItemModel.mediumId)) {
                    TrackListItem(
                        track = listItemModel,
                        filterText = filterText,
                        mostListenedTrackCount = uiState.mostListenedTrackCount,
                        onRecordingClick = onRecordingClick,
                        isSelected = selectedIds.map { it.id }.contains(listItemModel.id),
                        onSelect = onSelect,
                        onMoreActionsClick = { showBottomSheetForTrack = listItemModel },
                    )
                }
            }

            is ListSeparator -> {
                CollapsibleListSeparatorHeader(
                    text = listItemModel.text,
                    collapsed = uiState.collapsedMediumIds.contains(listItemModel.id.toLong()),
                    onClick = {
                        eventSink(TracksByReleaseUiEvent.ToggleMedium(listItemModel.id))
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
