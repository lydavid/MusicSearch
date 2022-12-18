package ly.david.mbjc.ui.series.details

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.SeriesListItemModel
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeadingRes
import ly.david.mbjc.ui.common.listitem.InformationListSeparatorHeader

@Composable
internal fun SeriesDetailsScreen(
    modifier: Modifier = Modifier,
    series: SeriesListItemModel,
) {
    Column(modifier = modifier) {
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
