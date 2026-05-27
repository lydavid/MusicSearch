package ly.david.musicsearch.shared.feature.details.place

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.feature.details.area.AreaSection
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.place.CoordinateListItem
import ly.david.musicsearch.ui.common.place.getDisplayString
import ly.david.musicsearch.ui.common.text.TextWithHeading
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.address
import musicsearch.ui.common.generated.resources.closed
import musicsearch.ui.common.generated.resources.coordinates
import musicsearch.ui.common.generated.resources.opened
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlaceDetailsTabUi(
    place: PlaceDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
    snackbarHostState: SnackbarHostState,
) {
    val eventSink = detailsTabUiState.eventSink

    DetailsTabUi(
        detailsModel = place,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        snackbarHostState = snackbarHostState,
        entityInfoSection = {
            type?.getDisplayString()?.let {
                TextWithHeading(
                    heading = stringResource(Res.string.type),
                    text = it,
                    filterText = filterText,
                )
            }
            lifeSpan.run {
                begin.ifNotEmpty {
                    TextWithHeading(
                        heading = stringResource(Res.string.opened),
                        text = it,
                        filterText = filterText,
                    )
                }
                end.ifNotEmpty {
                    TextWithHeading(
                        heading = stringResource(Res.string.closed),
                        text = it,
                        filterText = filterText,
                    )
                }
            }
            address.ifNotEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.address),
                    text = it,
                    filterText = filterText,
                )
            }
        },
        bringYourOwnLabelsSection = {
            place.area?.ifNotNull { area ->
                item {
                    AreaSection(
                        areaListItemModel = area,
                        filterText = filterText,
                        onItemClick = onItemClick,
                        collapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.Area),
                        onCollapseExpand = {
                            eventSink(DetailsTabUiEvent.ToggleCollapseExpandSection(CollapsibleSection.Area))
                        },
                    )
                }
            }

            val coordinates = place.coordinates
            val showCoordinates = coordinates.latitude != null && coordinates.longitude != null
            if (showCoordinates) {
                item {
                    // TODO: either support collapsing coordinates, or move it under place information
                    //  but then we need to support clicking on rows there
                    ListSeparatorHeader(stringResource(Res.string.coordinates))
                    val label = place.name +
                        if (place.lifeSpan.ended) " (${stringResource(Res.string.closed)})" else ""
                    CoordinateListItem(
                        coordinates = coordinates,
                        label = label,
                    )
                }
            }
        },
    )
}
