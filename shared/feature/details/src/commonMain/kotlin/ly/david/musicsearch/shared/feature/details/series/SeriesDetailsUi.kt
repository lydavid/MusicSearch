package ly.david.musicsearch.shared.feature.details.series

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.series.SeriesDetailsModel
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.UrlsSection

@Composable
internal fun SeriesDetailsUi(
    series: SeriesDetailsModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            series.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.series))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }

                // TODO: not enough info to warrant its own tab?
                //  move to subtitle

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                )
            }
        }
    }
}
