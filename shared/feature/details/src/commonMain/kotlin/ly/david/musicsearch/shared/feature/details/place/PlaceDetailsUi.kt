package ly.david.musicsearch.shared.feature.details.place

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.domain.place.PlaceDetailsModel
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.place.CoordinateListItem
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection

@Composable
internal fun PlaceDetailsUi(
    place: PlaceDetailsModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            place.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.place))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                lifeSpan.run {
                    begin?.ifNotNullOrEmpty {
                        TextWithHeading(
                            heading = strings.opened,
                            text = it,
                            filterText = filterText,
                        )
                    }
                    end?.ifNotNullOrEmpty {
                        TextWithHeading(
                            heading = strings.closed,
                            text = it,
                            filterText = filterText,
                        )
                    }
                }
                address.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.address,
                        text = it,
                        filterText = filterText,
                    )
                }

                area?.ifNotNull {
                    val areaName = it.getNameWithDisambiguation()
                    if (areaName.contains(filterText, ignoreCase = true)) {
                        ListSeparatorHeader(strings.area)
                        AreaListItem(
                            area = it,
                            showType = false,
                            onAreaClick = {
                                onItemClick(
                                    MusicBrainzEntity.AREA,
                                    id,
                                    areaName,
                                )
                            },
                            showIcon = false,
                        )
                    }
                }

                coordinates.let {
                    ListSeparatorHeader(strings.coordinates)
                    val label = place.name +
                        if (place.lifeSpan.ended == true) " (${strings.closed})" else ""
                    CoordinateListItem(
                        coordinates = it,
                        label = label,
                    )
                }

                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )
            }
        }
        urlsSection(
            urls = place.urls,
        )
    }
}
