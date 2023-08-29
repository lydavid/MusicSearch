package ly.david.mbjc.ui.place.details

import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ly.david.data.CoordinatesUiModel
import ly.david.data.LifeSpanUiModel
import ly.david.data.common.ifNotNull
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.domain.place.PlaceScaffoldModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzEntity
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.ui.common.R
import ly.david.ui.common.area.AreaListItem
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.place.CoordinateListItem
import ly.david.ui.common.text.TextWithHeadingRes
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
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            place.run {
                InformationListSeparatorHeader(R.string.place)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                lifeSpan?.run {
                    begin?.ifNotNullOrEmpty {
                        TextWithHeadingRes(
                            headingRes = R.string.opened,
                            text = it,
                            filterText = filterText,
                        )
                    }
                    end?.ifNotNullOrEmpty {
                        TextWithHeadingRes(
                            headingRes = R.string.closed,
                            text = it,
                            filterText = filterText,
                        )
                    }
                }
                address.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.address,
                        text = it,
                        filterText = filterText,
                    )
                }

                area?.ifNotNull {
                    val areaName = it.getNameWithDisambiguation()
                    if (areaName.contains(filterText)) {
                        ListSeparatorHeader(text = stringResource(id = R.string.area))
                        AreaListItem(
                            area = it,
                            showType = false,
                            onAreaClick = {
                                onItemClick(MusicBrainzEntity.AREA, id, areaName)
                            }
                        )
                    }
                }

                coordinates?.let {
                    ListSeparatorHeader(text = stringResource(id = R.string.coordinates))
                    val label = place.name +
                        if (place.lifeSpan?.ended == true) " (${stringResource(id = R.string.closed)})" else ""
                    CoordinateListItem(
                        context = context,
                        coordinates = it,
                        label = label
                    )
                }

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

// region Previews
@ExcludeFromJacocoGeneratedReport
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
                        ended = true
                    ),
                    area = AreaListItemModel(
                        id = "a1",
                        "Area",
                        "that one"
                    )
                )
            )
        }
    }
}
// endregion
