package ly.david.mbjc.ui.recording

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.data.domain.RecordingUiModel
import ly.david.data.navigation.Destination

// TODO: Do we want to generalize this for places? and any screens that uses relations?
//  might be better to copy/paste and tailor them
@Composable
internal fun RecordingScreen(
    modifier: Modifier = Modifier,
    recordingId: String,
    onTitleUpdate: (title: String, subtitle: String) -> Unit = { _, _ -> },
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },

) {

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }
    var recording: RecordingUiModel? by remember { mutableStateOf(null) }
    val lazyListState = rememberLazyListState()

//    LaunchedEffect(key1 = recordingId) {
//        try {
//            recording = viewModel.lookupRecording(recordingId)
//            onTitleUpdate(
//                recording?.getNameWithDisambiguation() ?: "[should not happen]",
//                "[Recording by <artist name>]"
//            )
//        } catch (ex: Exception) {
//            onTitleUpdate("[Recording lookup failed]", "[error]")
//        }
//        lookupInProgress = false
//    }



//    PagingLoadingAndErrorHandler(
//        modifier = modifier,
//        lazyPagingItems = lazyPagingItems,
//        somethingElseLoading = lookupInProgress,
//        lazyListState = lazyListState,
//    ) { uiModel: UiModel? ->
//
//        when (uiModel) {
//            is ly.david.data.domain.Header -> {
//                Column {
//                    Text(text = recording?.length.toDisplayTime())
//                    Text(text = recording?.date.orEmpty())
//                }
//            }
//            is ly.david.data.domain.RelationUiModel -> {
//                RelationCard(
//                    relation = uiModel,
//                    onItemClick = onItemClick,
//                )
//            }
//            else -> {
//                // Do nothing.
//            }
//        }
//    }
}
