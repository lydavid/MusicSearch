package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNull
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
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.image.LargeImage
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.core.LocalStrings

fun <T : MusicBrainzDetailsModel> T.getCapitalizedName(strings: AppStrings): String {
    return when (this) {
        is AreaDetailsModel -> strings.area
        is ArtistDetailsModel -> strings.artist
        is EventDetailsModel -> strings.event
        is InstrumentDetailsModel -> strings.instrument
        is LabelDetailsModel -> strings.label
        is PlaceDetailsModel -> strings.place
        is RecordingDetailsModel -> strings.recording
        is ReleaseDetailsModel -> strings.release
        is ReleaseGroupDetailsModel -> strings.releaseGroup
        is SeriesDetailsModel -> strings.series
        is WorkDetailsModel -> strings.work
    }
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
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = detailsTabUiState.lazyListState,
    ) {
        detailsModel.run {
            item {
                if (filterText.isBlank()) {
                    LargeImage(
                        url = imageMetadata.largeUrl,
                        placeholderKey = imageMetadata.imageId,
                        onClick = onImageClick,
                    )
                }

                ListSeparatorHeader(text = strings.informationHeader(getCapitalizedName(strings)))

                // TODO: better location for number of images? I put it here to be close to the image itself
                //  but it's not part of the entity's information
                detailsTabUiState.numberOfImages?.ifNotNull {
                    TextWithHeading(
                        heading = strings.numberOfImages,
                        text = "$it",
                        filterText = filterText,
                    )
                }
                entityInfoSection()
            }

            additionalDetailsSection?.run {
                item {
                    ListSeparatorHeader(strings.additionalDetails)
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
                urls = urls,
                collapsed = detailsTabUiState.isExternalLinksCollapsed,
                onCollapseExpand = onCollapseExpandExternalLinks,
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
