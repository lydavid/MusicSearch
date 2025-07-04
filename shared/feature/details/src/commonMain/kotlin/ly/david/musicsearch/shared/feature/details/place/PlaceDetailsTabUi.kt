package ly.david.musicsearch.shared.feature.details.place

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.place.CoordinateListItem
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
internal fun PlaceDetailsTabUi(
    place: PlaceDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onCollapseExpandExternalLinks: () -> Unit = {},
) {
    val strings = LocalStrings.current

    DetailsTabUi(
        detailsModel = place,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        entityInfoSection = {
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
        },
    )
}
