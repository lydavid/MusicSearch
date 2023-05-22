package ly.david.mbjc.ui.recording.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.common.ifNotNull
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.common.toDisplayTime
import ly.david.data.domain.RecordingScaffoldModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.TextWithHeadingRes
import ly.david.mbjc.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.R
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme

@Composable
internal fun RecordingDetailsScreen(
    modifier: Modifier = Modifier,
    recording: RecordingScaffoldModel,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            recording.run {
                InformationListSeparatorHeader(R.string.recording)
                length?.ifNotNull {
                    TextWithHeadingRes(headingRes = R.string.length, text = it.toDisplayTime())
                }
                firstReleaseDate?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.first_release_date, text = it)
                }
                isrcs?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.isrc, text = it.joinToString("\n"))
                }
            }
        }
    }
}

// region Previews
@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            RecordingDetailsScreen(
                recording = RecordingScaffoldModel(
                    id = "132a508b-624a-4f1d-b61f-f6616121bab5",
                    name = "プライド革命",
                    length = 235000,
                    isrcs = listOf(
                        "JPX401500068"
                    )
                )
            )
        }
    }
}
// endregion
