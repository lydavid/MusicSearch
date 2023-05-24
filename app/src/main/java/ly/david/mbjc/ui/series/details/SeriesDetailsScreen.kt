package ly.david.mbjc.ui.series.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.SeriesListItemModel
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.R

@Composable
internal fun SeriesDetailsScreen(
    modifier: Modifier = Modifier,
    series: SeriesListItemModel,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            series.run {
                InformationListSeparatorHeader(R.string.series)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.type, text = it)
                }

                // TODO: not enough info to warrant its own tab?
                //  move to subtitle
            }
        }
    }
}
