package ly.david.musicsearch.ui.common.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@Composable
fun RecordingsListScreen(
    state: RecordingsListUiState,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = state.lazyPagingItems,
        modifier = modifier,
        lazyListState = state.lazyListState,
    ) { recordingListItemModel: RecordingListItemModel? ->
        when (recordingListItemModel) {
            is RecordingListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        RecordingListItem(
                            recording = recordingListItemModel,
                        ) {
                            onItemClick(
                                MusicBrainzEntity.RECORDING,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
                    onDelete = {
                        onDeleteFromCollection?.invoke(
                            recordingListItemModel.id,
                            recordingListItemModel.name,
                        )
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
