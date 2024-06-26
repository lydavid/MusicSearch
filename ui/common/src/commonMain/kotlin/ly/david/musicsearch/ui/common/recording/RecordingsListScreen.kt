package ly.david.musicsearch.ui.common.recording

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordingsListScreen(
    lazyPagingItems: LazyPagingItems<RecordingListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    onRecordingClick: (entity: MusicBrainzEntity, String, String) -> Unit,
) {
    ScreenWithPagingLoadingAndError(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState,
    ) { recordingListItemModel: RecordingListItemModel? ->
        when (recordingListItemModel) {
            is RecordingListItemModel -> {
                RecordingListItem(
                    recording = recordingListItemModel,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    onRecordingClick(
                        MusicBrainzEntity.RECORDING,
                        id,
                        getNameWithDisambiguation(),
                    )
                }
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
