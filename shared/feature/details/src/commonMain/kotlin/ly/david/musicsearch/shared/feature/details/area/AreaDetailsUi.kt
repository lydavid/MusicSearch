package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.area.AreaDetailsModel
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun AreaDetailsUi(
    area: AreaDetailsModel,
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
            area.run {
                // TODO: Consider passing the string mapping of MusicBrainzEntity to these instead
                ListSeparatorHeader(text = strings.informationHeader(strings.area))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                LifeSpanText(
                    lifeSpan = lifeSpan,
                    heading = strings.date,
                    beginHeading = strings.startDate,
                    endHeading = strings.endDate,
                    filterText = filterText,
                )
                countryCode.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.iso31661,
                        text = it,
                        filterText = filterText,
                    )
                    TextWithHeading(
                        heading = strings.regionalIndicatorSymbol,
                        text = it.toFlagEmoji(),
                        filterText = filterText,
                    )
                }

                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )

                // TODO: api doesn't seem to include area containment
                //  but we could get its parent area via relations "part of" "backward"
            }
        }

        urlsSection(
            urls = area.urls,
        )
    }
}
