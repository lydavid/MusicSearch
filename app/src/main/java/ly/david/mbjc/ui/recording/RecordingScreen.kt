package ly.david.mbjc.ui.recording

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.persistence.recording.RecordingRelationRoomModel
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.navigation.Destination

// TODO: This can be generalized for Work relations too
@Composable
internal fun RecordingScreen(
    modifier: Modifier = Modifier,
    recordingId: String,
    onTitleUpdate: (title: String, subtitle: String) -> Unit = { _, _ -> },
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: RecordingViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = recordingId) {
        try {
            onTitleUpdate(
                viewModel.lookupRecording(recordingId).getNameWithDisambiguation(),
                "[Recording by <artist name>]"
            )
        } catch (ex: Exception) {
            onTitleUpdate("[Recording lookup failed]", "[error]")
        }
    }

    val lazyPagingItems: LazyPagingItems<RecordingRelationRoomModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
            .collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
//        snackbarHostState = snackbarHostState
    ) { recordingRelation ->

        // TODO: could put non-clickable items for length/first release date
        
        if (recordingRelation == null) return@PagingLoadingAndErrorHandler
        RecordingRelationCard(
            relation = recordingRelation,
            onItemClick = onItemClick,
        )
    }
}
