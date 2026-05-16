package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
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
import ly.david.musicsearch.shared.domain.details.asEntity
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.tag.TagRepository
import ly.david.musicsearch.shared.feature.details.alias.aliasesSection
import ly.david.musicsearch.shared.feature.details.alias.toAliasListItemModel
import ly.david.musicsearch.shared.feature.details.tag.genresAndTagsSection
import ly.david.musicsearch.shared.feature.details.tag.getMessage
import ly.david.musicsearch.ui.common.image.LargeImage
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.screen.NavigatableFromOverlayResult
import ly.david.musicsearch.ui.common.screen.SnackbarPopResult
import ly.david.musicsearch.ui.common.screen.TagDetailsScreen
import ly.david.musicsearch.ui.common.screen.showInBottomSheetForResult
import ly.david.musicsearch.ui.common.snackbar.FeedbackSnackbarVisuals
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

// TODO: consider passing event sink to this layer, and invoking here, instead of reimplementing 10 times
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
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onGoToScreen: (screen: NavigatableFromOverlayResult) -> Unit,
    onRefreshLocal: () -> Unit,
    onLoginClick: () -> Unit,
) {
    val primaryLabel = stringResource(Res.string.primary)
    val aliases = detailsModel.aliases.map { it.toAliasListItemModel() }.toPersistentList()
    val overlayHost = LocalOverlayHost.current
    val coroutineScope = rememberCoroutineScope()

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
                onChipClick = {
                    coroutineScope.launch {
                        val result: SnackbarPopResult<CommonParcelable> =
                            overlayHost.showInBottomSheetForResult(
                                screen = TagDetailsScreen(
                                    entity = detailsModel.asEntity(),
                                    genreOrTag = it,
                                ),
                            )
                        result.feedback?.let { feedback ->
                            when (feedback) {
                                is NavigatableFromOverlayResult -> onGoToScreen(feedback)
                                is Feedback<*> -> {
                                    val tagFeedback = feedback as? Feedback<TagRepository.TagFeedback> ?: return@let

                                    if (tagFeedback.data is TagRepository.TagFeedback.Voted) {
                                        onRefreshLocal()
                                    }

                                    val message = (tagFeedback).getMessage()
                                    val snackbarResult = snackbarHostState.showSnackbar(
                                        visuals = FeedbackSnackbarVisuals(
                                            message = message,
                                            actionLabel = (tagFeedback as? Feedback.Error)?.action?.name,
                                            duration = when (tagFeedback) {
                                                is Feedback.Loading -> SnackbarDuration.Indefinite
                                                is Feedback.Success,
                                                is Feedback.Error,
                                                is Feedback.Actionable,
                                                -> SnackbarDuration.Short
                                            },
                                            withDismissAction = false,
                                            feedback = tagFeedback,
                                        ),
                                    )
                                    when (snackbarResult) {
                                        SnackbarResult.ActionPerformed -> {
                                            onLoginClick()
                                        }

                                        SnackbarResult.Dismissed -> {
                                            // Do nothing.
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
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
