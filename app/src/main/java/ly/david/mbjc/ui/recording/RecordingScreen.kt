package ly.david.mbjc.ui.recording

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.data.getNameWithDisambiguation

// TODO: unlike the screens before, the main screen just displays all the details/rels of this recording
@Composable
internal fun RecordingScreen(
    modifier: Modifier = Modifier,
    recordingId: String,
    onTitleUpdate: (title: String, subtitle: String) -> Unit = { _, _ -> },
    viewModel: RecordingViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = recordingId) {
        try {
            onTitleUpdate(viewModel.lookupRecording(recordingId).getNameWithDisambiguation(), "[TODO]")
        } catch (e: Exception) {
            onTitleUpdate("[Recording lookup failed]", "[error]")
        }
    }

//    val lazyPagingItems: LazyPagingItems<UiModel> = rememberFlowWithLifecycleStarted(viewModel.pagedTracks).collectAsLazyPagingItems()

    // TODO: display details of this recording
    //  cards: display all relations



    LazyColumn(
        modifier = modifier
    ) {
        item {
            Text(text = "hi")
        }
//        items(lazyPagingItems) {
//
//        }
    }
}
