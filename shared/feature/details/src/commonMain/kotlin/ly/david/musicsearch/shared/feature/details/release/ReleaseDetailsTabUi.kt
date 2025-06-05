package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.UNKNOWN_TIME
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.label.LabelListItem
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.work.getDisplayLanguage
import ly.david.musicsearch.ui.common.work.getDisplayScript
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun ReleaseDetailsTabUi(
    release: ReleaseDetailsModel,
    detailsTabUiState: DetailsTabUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onCollapseExpandReleaseEvents: () -> Unit = {},
    onCollapseExpandExternalLinks: () -> Unit = {},
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    val entityInfoSection: @Composable ReleaseDetailsModel.() -> Unit = {
        barcode?.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.barcode,
                text = it,
                filterText = filterText,
            )
        }
        formattedFormats.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.format,
                text = it,
                filterText = filterText,
            )
        }
        formattedTracks.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.tracks,
                text = it,
                filterText = filterText,
            )
        }

        val releaseLength = releaseLength.toDisplayTime()
        val formattedReleaseLength = if (hasNullLength) {
            if (releaseLength == UNKNOWN_TIME) UNKNOWN_TIME else "$releaseLength (+ $UNKNOWN_TIME)"
        } else {
            releaseLength
        }
        TextWithHeading(
            heading = strings.length,
            text = formattedReleaseLength,
            filterText = filterText,
        )

        date.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.date,
                text = it,
                filterText = filterText,
            )
        }
    }
    val additionalDetailsSection: @Composable ReleaseDetailsModel.() -> Unit = {
        releaseGroup?.let {
            TextWithHeading(
                heading = strings.type,
                text = it.getDisplayTypes(),
                filterText = filterText,
            )
        }
        packaging?.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.packaging,
                text = it,
                filterText = filterText,
            )
        }
        status?.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.status,
                text = it,
                filterText = filterText,
            )
        }
        textRepresentation.language?.getDisplayLanguage(strings).ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.language,
                text = it,
                filterText = filterText,
            )
        }
        textRepresentation.script?.getDisplayScript(strings).ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.script,
                text = it,
                filterText = filterText,
            )
        }
        quality?.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.dataQuality,
                text = it,
                filterText = filterText,
            )
        }
        asin?.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.asin,
                text = it,
                filterText = filterText,
            )
        }
    }
    val bringYourOwnLabelsSection: LazyListScope.() -> Unit = {
        release
            .copy(
                labels = release.labels.filter { label ->
                    val searchText = filterText.lowercase()
                    listOf(
                        label.getNameWithDisambiguation(),
                        label.type,
                        label.labelCode.toString(),
                        label.catalogNumbers,
                    ).any { it?.lowercase()?.contains(searchText) == true }
                },
                areas = release.areas.filter { area ->
                    val searchText = filterText.lowercase()
                    listOf(
                        area.getNameWithDisambiguation(),
                        area.date,
                    ).any { it?.lowercase()?.contains(searchText) == true }
                },
            )
            .run {
                item {
                    labels.ifNotNullOrEmpty {
                        ListSeparatorHeader(strings.labels)
                    }
                }
                items(labels) { label ->
                    LabelListItem(
                        label = label,
                        onLabelClick = {
                            onItemClick(
                                MusicBrainzEntity.LABEL,
                                id,
                                name,
                            )
                        },
                        showIcon = false,
                    )
                }

                val collapsed = detailsTabUiState.isReleaseEventsCollapsed
                item {
                    areas.ifNotNullOrEmpty {
                        CollapsibleListSeparatorHeader(
                            text = strings.releaseEvents,
                            collapsed = collapsed,
                            onClick = onCollapseExpandReleaseEvents,
                        )
                    }
                }
                if (!collapsed) {
                    items(areas) { area: AreaListItemModel ->
                        AreaListItem(
                            area = area,
                            showType = false,
                            showIcon = false,
                            onAreaClick = {
                                onItemClick(
                                    MusicBrainzEntity.AREA,
                                    id,
                                    name,
                                )
                            },
                        )
                    }
                }
            }
    }
    DetailsTabUi(
        detailsModel = release,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onImageClick = onImageClick,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        entityInfoSection = entityInfoSection,
        additionalDetailsSection = additionalDetailsSection,
        bringYourOwnLabelsSection = bringYourOwnLabelsSection,
    )
}
