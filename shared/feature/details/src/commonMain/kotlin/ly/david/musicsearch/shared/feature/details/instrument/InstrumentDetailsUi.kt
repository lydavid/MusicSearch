package ly.david.musicsearch.shared.feature.details.instrument

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.instrument.InstrumentDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
internal fun InstrumentDetailsUi(
    instrument: InstrumentDetailsModel,
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
            instrument.run {
                ListSeparatorHeader(strings.informationHeader(strings.instrument))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }

                ListSeparatorHeader(strings.description)
                description?.ifNotNullOrEmpty {
                    SelectionContainer {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 4.dp,
                                ),
                            text = it,
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }
                }

                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )
            }
        }
        urlsSection(
            urls = instrument.urls,
        )
    }
}
