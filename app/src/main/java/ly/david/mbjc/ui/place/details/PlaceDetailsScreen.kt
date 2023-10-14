package ly.david.mbjc.ui.place.details

import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ly.david.musicsearch.data.core.common.ifNotNull
import ly.david.musicsearch.data.core.common.ifNotNullOrEmpty
import ly.david.musicsearch.data.core.getNameWithDisambiguation
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.AreaListItemModel
import ly.david.musicsearch.data.core.place.CoordinatesUiModel
import ly.david.musicsearch.data.core.place.PlaceScaffoldModel
import ly.david.ui.common.area.AreaListItem
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.place.CoordinateListItem
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun PlaceDetailsScreen(
    place: PlaceScaffoldModel,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
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
                                onItemClick(MusicBrainzEntity.AREA, id, areaName)
                            },
                        )
                    }
                }

                coordinates?.let {
                    ListSeparatorHeader(strings.coordinates)
                    val label = place.name +
                        if (place.lifeSpan?.ended == true) " (${strings.closed})" else ""
                    CoordinateListItem(
                        context = context,
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
