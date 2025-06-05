package ly.david.musicsearch.shared.feature.details.label

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.label.LabelDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection

@Composable
internal fun LabelDetailsUi(
    label: LabelDetailsModel,
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
            label.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.label))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                labelCode?.ifNotNull {
                    TextWithHeading(
                        heading = strings.labelCode,
                        text = strings.lc(it),
                        filterText = filterText,
                    )
                }

                ipis?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.ipi,
                        text = it.joinToString(", "),
                        filterText = filterText,
                    )
                }

                isnis?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.isni,
                        text = it.joinToString(", "),
                        filterText = filterText,
                    )
                }

                // TODO: lifespan, founded, defunct for end

                // TODO: area

                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )
            }
        }
        urlsSection(
            urls = label.urls,
        )
    }
}
