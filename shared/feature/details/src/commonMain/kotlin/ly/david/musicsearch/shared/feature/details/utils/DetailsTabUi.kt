package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.feature.details.alias.aliasesSection
import ly.david.musicsearch.ui.common.image.LargeImage
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.additionalDetails
import musicsearch.ui.common.generated.resources.area
import musicsearch.ui.common.generated.resources.artist
import musicsearch.ui.common.generated.resources.event
import musicsearch.ui.common.generated.resources.informationHeader
import musicsearch.ui.common.generated.resources.instrument
import musicsearch.ui.common.generated.resources.label
import musicsearch.ui.common.generated.resources.place
import musicsearch.ui.common.generated.resources.recording
import musicsearch.ui.common.generated.resources.release
import musicsearch.ui.common.generated.resources.releaseGroup
import musicsearch.ui.common.generated.resources.series
import musicsearch.ui.common.generated.resources.work
import org.jetbrains.compose.resources.stringResource

@Composable
private fun <T : MusicBrainzDetailsModel> T.getCapitalizedName(): String {
    return stringResource(
        when (this) {
            is AreaDetailsModel -> Res.string.area
            is ArtistDetailsModel -> Res.string.artist
            is EventDetailsModel -> Res.string.event
            is InstrumentDetailsModel -> Res.string.instrument
            is LabelDetailsModel -> Res.string.label
            is PlaceDetailsModel -> Res.string.place
            is RecordingDetailsModel -> Res.string.recording
            is ReleaseDetailsModel -> Res.string.release
            is ReleaseGroupDetailsModel -> Res.string.releaseGroup
            is SeriesDetailsModel -> Res.string.series
            is WorkDetailsModel -> Res.string.work
        },
    )
}

@Composable
internal fun <T : MusicBrainzDetailsModel> DetailsTabUi(
    detailsModel: T,
    detailsTabUiState: DetailsTabUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    entityInfoSection: @Composable T.() -> Unit = {},
    additionalDetailsSection: @Composable (T.() -> Unit)? = null,
    bringYourOwnLabelsSection: LazyListScope.() -> Unit = {},
    onImageClick: () -> Unit = {},
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        state = detailsTabUiState.lazyListState,
    ) {
        detailsModel.run {
            item {
                if (filterText.isBlank()) {
                    LargeImage(
                        url = imageMetadata.largeUrl,
                        imageId = imageMetadata.imageId,
                        onClick = onImageClick,
                    )
                }

                ListSeparatorHeader(
                    text = stringResource(Res.string.informationHeader, getCapitalizedName()),
                    numberOfImages = detailsTabUiState.numberOfImages,
                )

                entityInfoSection()
            }

            additionalDetailsSection?.run {
                item {
                    ListSeparatorHeader(stringResource(Res.string.additionalDetails))
                    invoke(detailsModel)
                }
            }

            item {
                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )
            }

            bringYourOwnLabelsSection()

            urlsSection(
                filteredUrls = urls,
                totalUrls = detailsTabUiState.totalUrls,
                collapsed = detailsTabUiState.isExternalLinksCollapsed,
                onCollapseExpand = onCollapseExpandExternalLinks,
            )

            aliasesSection(
                filteredAliases = aliases,
                totalAliases = detailsTabUiState.totalAliases,
                collapsed = detailsTabUiState.isAliasesCollapsed,
                onCollapseExpand = onCollapseExpandAliases,
            )

            item {
                LastUpdatedFooterItem(
                    lastUpdated = lastUpdated,
                    now = detailsTabUiState.now,
                )
            }
        }
    }
}
