package ly.david.mbjc.ui.common.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.RecordingCardModel
import ly.david.data.getNameWithDisambiguation
import ly.david.mbjc.ui.recording.RecordingCard

@Composable
internal fun RecordingsListScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<RecordingCardModel>,
    onRecordingClick: (String, String) -> Unit,
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { recordingCardModel: RecordingCardModel? ->
        when (recordingCardModel) {
            is RecordingCardModel -> {
                RecordingCard(recording = recordingCardModel) {
                    onRecordingClick(id, getNameWithDisambiguation())
                }
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
