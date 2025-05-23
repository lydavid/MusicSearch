package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.UNKNOWN_TIME
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.domain.release.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.image.LargeImage
import ly.david.musicsearch.ui.common.label.LabelListItem
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.common.work.getDisplayLanguage
import ly.david.musicsearch.ui.common.work.getDisplayScript
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun ReleaseDetailsUi(
    release: ReleaseDetailsModel,
    releaseDetailsUiState: ReleaseDetailsUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onCollapseExpand: () -> Unit = {},
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = releaseDetailsUiState.lazyListState,
    ) {
        release.run {
            item {
                if (filterText.isBlank()) {
                    LargeImage(
                        url = release.imageMetadata.largeUrl,
                        placeholderKey = release.imageMetadata.databaseId.toString(),
                        onClick = onImageClick,
                    )
                }
                ListSeparatorHeader(text = strings.informationHeader(strings.release))
                releaseDetailsUiState.numberOfImages?.ifNotNull {
                    TextWithHeading(
                        heading = strings.numberOfImages,
                        text = "$it",
                        filterText = filterText,
                    )
                }
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

                ListSeparatorHeader(strings.additionalDetails)
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

            item {
                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )
            }

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

            val collapsed = releaseDetailsUiState.isReleaseEventsCollapsed
            item {
                areas.ifNotNullOrEmpty {
                    CollapsibleListSeparatorHeader(
                        text = strings.releaseEvents,
                        collapsed = collapsed,
                        onClick = onCollapseExpand,
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

            urlsSection(
                urls = urls,
            )
        }
    }
}
