package ly.david.mbjc.ui.work.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import java.util.Locale
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.WorkListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun WorkDetailsScreen(
    modifier: Modifier = Modifier,
    work: WorkListItemModel,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            work.run {
                ListSeparatorHeader(text = stringResource(id = R.string.work_information))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.type, text = it)
                }
                language?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.language, text = Locale(it).displayLanguage)
                }
                iswcs?.ifNotNullOrEmpty {
                    TextWithHeading(headingRes = R.string.iswc, text = it.joinToString("\n"))
                }

                // TODO: work attributes
//                labels.ifNotNullOrEmpty {
//                    ListSeparatorHeader(text = stringResource(id = R.string.labels))
//                    it.forEach { label ->
//                        LabelListItem(label = label, onLabelClick = onLabelClick)
//                    }
//                }
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
            WorkDetailsScreen(
                work = WorkListItemModel(
                    id = "w1",
                    name = "Work",
                    type = "Song",
                    language = "eng",
                    iswcs = listOf(
                        "T-101.238.335-4",
                        "T-101.238.335-5",
                    )
                )
            )
        }
    }
}
// endregion
