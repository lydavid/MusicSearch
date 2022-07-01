package ly.david.mbjc.ui.recording

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.Recording
import ly.david.mbjc.data.domain.Header
import ly.david.mbjc.data.domain.RecordingRelationUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.toDisplayTime
import ly.david.mbjc.ui.navigation.Destination

// TODO: Do we want to generalize this for places? and any screens that uses relations?
//  might be better to copy/paste and tailor them
@Composable
internal fun RecordingScreen(
    modifier: Modifier = Modifier,
    recordingId: String,
    onTitleUpdate: (title: String, subtitle: String) -> Unit = { _, _ -> },
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: RecordingViewModel = hiltViewModel()
) {

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }
    var recording: Recording? by remember { mutableStateOf(null) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = recordingId) {
        try {
            recording = viewModel.lookupRecording(recordingId)
            onTitleUpdate(
                recording?.getNameWithDisambiguation() ?: "[should not happen]",
                "[Recording by <artist name>]"
            )
        } catch (ex: Exception) {
            onTitleUpdate("[Recording lookup failed]", "[error]")
        }
        lookupInProgress = false
    }

    val lazyPagingItems: LazyPagingItems<UiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
            .collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        somethingElseLoading = lookupInProgress,
        lazyListState = lazyListState,
        prependedItems = {

        }
    ) { uiModel: UiModel? ->

        when (uiModel) {
            is Header -> {
                Column {
                    Text(text = recording?.length.toDisplayTime())
                    Text(text = recording?.date.orEmpty())
                }
            }
            is RecordingRelationUiModel -> {
                RecordingRelationCard(
                    relation = uiModel,
                    onItemClick = onItemClick,
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
