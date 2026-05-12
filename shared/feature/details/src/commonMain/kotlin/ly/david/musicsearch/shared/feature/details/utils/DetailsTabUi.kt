package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toPersistentList
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
import ly.david.musicsearch.shared.domain.tag.GenreOrTag
import ly.david.musicsearch.shared.feature.details.alias.aliasesSection
import ly.david.musicsearch.shared.feature.details.alias.toAliasListItemModel
import ly.david.musicsearch.ui.common.image.LargeImage
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.area
import musicsearch.ui.common.generated.resources.artist
import musicsearch.ui.common.generated.resources.event
import musicsearch.ui.common.generated.resources.informationHeader
import musicsearch.ui.common.generated.resources.instrument
import musicsearch.ui.common.generated.resources.label
import musicsearch.ui.common.generated.resources.place
import musicsearch.ui.common.generated.resources.primary
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T : MusicBrainzDetailsModel> DetailsTabUi(
    detailsModel: T,
    detailsTabUiState: DetailsTabUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    entityInfoSection: @Composable T.() -> Unit = {},
    bringYourOwnLabelsSection: LazyListScope.() -> Unit = {},
    onImageClick: () -> Unit = {},
    onCollapseExpandSection: (CollapsibleSection) -> Unit,
    onSearchGenreOrTag: (String) -> Unit,
    onGoToGenre: (id: String) -> Unit,
) {
    val primaryLabel = stringResource(Res.string.primary)
    val aliases = detailsModel.aliases.map { it.toAliasListItemModel() }.toPersistentList()

    var showBottomSheetForGenreOrTag: GenreOrTag? by remember { mutableStateOf(null) }
    showBottomSheetForGenreOrTag?.let { genreOrTag ->
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheetForGenreOrTag = null
            },
        ) {
            TagBottomSheetContent(
                genreOrTag = genreOrTag,
                onSearchGenreOrTag = onSearchGenreOrTag,
                onGoToGenre = onGoToGenre,
                onDismiss = { showBottomSheetForGenreOrTag = null },
            )
        }
    }

    LazyColumn(
        modifier = modifier,
        state = detailsTabUiState.lazyListState,
    ) {
        detailsModel.run {
            item {
                if (filterText.isBlank()) {
                    LargeImage(
                        imageMetadata = detailsTabUiState.imageMetadata,
                        onClick = onImageClick,
                    )
                }

                ListSeparatorHeader(
                    text = stringResource(Res.string.informationHeader, getCapitalizedName()),
                    numberOfImages = detailsTabUiState.numberOfImages,
                )

                entityInfoSection()
            }

            genresAndTagsSection(
                genres = genres,
                tags = tags,
                filterText = filterText,
                isGenresCollapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.Genres),
                isTagsCollapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.Tags),
                onCollapseExpand = onCollapseExpandSection,
                onChipClick = { showBottomSheetForGenreOrTag = it },
            )

            item {
                WikipediaSection(
                    extract = detailsTabUiState.wikipediaExtract,
                    filterText = filterText,
                )
            }

            bringYourOwnLabelsSection()

            urlsSection(
                urls = urls,
                filterText = filterText,
                collapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.ExternalLinks),
                onCollapseExpand = { onCollapseExpandSection(CollapsibleSection.ExternalLinks) },
            )

            aliasesSection(
                aliases = aliases,
                primaryLabel = primaryLabel,
                filterText = filterText,
                collapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.Aliases),
                onCollapseExpand = { onCollapseExpandSection(CollapsibleSection.Aliases) },
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
