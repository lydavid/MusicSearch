package ly.david.mbjc.ui.place.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.common.ifNotNull
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.place.CoordinatesUiModel
import ly.david.musicsearch.core.models.place.PlaceScaffoldModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.area.AreaListItem
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.commonlegacy.place.CoordinateListItem
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun PlaceDetailsScreen(
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

// region Previews
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            PlaceDetailsScreen(
                place = PlaceScaffoldModel(
                    id = "p1",
                    name = "Some Place",
                    type = "Venue",
                    address = "123 Fake St",
                    coordinates = CoordinatesUiModel(-123.4567, 123.4567),
                    lifeSpan = LifeSpanUiModel(
                        begin = "2022-01-01",
                        end = "2022-12-10",
                        ended = true,
                    ),
                    area = AreaListItemModel(
                        id = "a1",
                        "Area",
                        "that one",
                    ),
                ),
            )
        }
    }
}
// endregion