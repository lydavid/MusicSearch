package ly.david.mbjc.ui.work.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Locale
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.WorkListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.TextWithHeadingRes
import ly.david.mbjc.ui.common.listitem.AttributesListSeparatorHeader
import ly.david.mbjc.ui.common.listitem.InformationListSeparatorHeader
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme

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
                InformationListSeparatorHeader(R.string.work)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.type, text = it)
                }
                language?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.language, text = Locale(it).displayLanguage)
                }
                iswcs?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.iswc, text = it.joinToString("\n"))
                }

                if (attributes.isNotEmpty()) {
                    AttributesListSeparatorHeader(R.string.work)
                }
            }
        }

        items(work.attributes) { attribute ->
            TextWithHeading(heading = attribute.type, text = attribute.value)
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
