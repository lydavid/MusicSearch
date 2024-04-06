package ly.david.musicsearch.shared.feature.details.place

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.common.ifNotNull
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.place.PlaceScaffoldModel
import ly.david.ui.core.LocalStrings
import ly.david.ui.common.area.AreaListItem
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.place.CoordinateListItem
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection

@Composable
internal fun PlaceDetailsUi(
    place: PlaceScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
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
                lifeSpan?.run {
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
                    if (areaName.contains(filterText)) {
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
                        )
                    }
                }

                coordinates?.let {
                    ListSeparatorHeader(strings.coordinates)
                    val label = place.name +
                        if (place.lifeSpan?.ended == true) " (${strings.closed})" else ""
                    CoordinateListItem(
                        coordinates = it,
                        label = label,
                    )
                }

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}
