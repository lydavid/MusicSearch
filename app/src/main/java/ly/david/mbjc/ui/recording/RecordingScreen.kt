package ly.david.mbjc.ui.recording

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.persistence.recording.RecordingRelationRoomModel
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

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
            label = recordingRelation.label,
            name = recordingRelation.name,
            disambiguation = recordingRelation.disambiguation,
            attributes = recordingRelation.attributes
        )
    }
}

@Composable
internal fun RecordingRelationCard(
    label: String,
    name: String,
    disambiguation: String? = null,
    attributes: String? = null,
    onClick: () -> Unit = {}
) {
    ClickableListItem(onClick = onClick) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = "$label:",
                style = TextStyles.getCardBodyTextStyle()
            )
            Text(
                text = name,
                style = TextStyles.getCardTitleTextStyle()
            )

            if (!disambiguation.isNullOrEmpty()) {
                Text(
                    text = "($disambiguation)",
                    style = TextStyles.getCardBodyTextStyle(),
                    color = getSubTextColor()
                )
            }

            if (!attributes.isNullOrEmpty()) {
                Text(
                    text = "($attributes)",
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun RecordingRelationCardPreview() {
    PreviewTheme {
        Surface {
            RecordingRelationCard(
                label = "miscellaneous support",
                name = "Artist Name",
                disambiguation = "that guy",
                attributes = "task: director & organizer, label: value"
            )
        }
    }
}
