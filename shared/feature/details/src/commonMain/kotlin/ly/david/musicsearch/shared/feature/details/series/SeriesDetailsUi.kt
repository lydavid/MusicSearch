package ly.david.musicsearch.shared.feature.details.series

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.series.SeriesDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun SeriesDetailsUi(
    series: SeriesDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = detailsTabUiState.lazyListState,
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

                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )
            }
        }
        urlsSection(
            urls = series.urls,
        )
    }
}
