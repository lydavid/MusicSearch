package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.details.asEntity
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.LastListenedListItemWithMoreActions
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.relation.UrlListItem
import ly.david.musicsearch.ui.common.releasegroup.getDisplayString
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.text.TextWithIcon
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.firstReleaseDate
import musicsearch.ui.common.generated.resources.listens
import musicsearch.ui.common.generated.resources.seeAllListens
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
internal fun ReleaseGroupDetailsTabUi(
    releaseGroup: ReleaseGroupDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onItemClick: MusicBrainzItemClickHandler,
    snackbarHostState: SnackbarHostState,
) {
    val eventSink = detailsTabUiState.eventSink

    DetailsTabUi(
        detailsModel = releaseGroup,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        entityInfoSection = {
            TextWithHeading(
                heading = stringResource(Res.string.type),
                text = getDisplayString(),
                filterText = filterText,
            )
            firstReleaseDate.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.firstReleaseDate),
                    text = it,
                    filterText = filterText,
                )
            }
        },
        bringYourOwnLabelsSection = {
            releaseGroup.run {
                listenSection(
                    releaseGroup = this@run,
                    filterText = filterText,
                    now = detailsTabUiState.now,
                    collapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.Listens),
                    onCollapseExpand = {
                        eventSink(DetailsTabUiEvent.ToggleCollapseExpandSection(CollapsibleSection.Listens))
                    },
                    onItemClick = onItemClick,
                    onGoToListenAtEpochSeconds = { seconds ->
                        eventSink(
                            DetailsTabUiEvent.GoToScreen(
                                screen = ListensScreen(
                                    dateTimeEpochSeconds = seconds,
                                ),
                            ),
                        )
                    },
                    onSeeAllListensClick = {
                        eventSink(
                            DetailsTabUiEvent.GoToScreen(
                                screen = ListensScreen(
                                    entityFacet = releaseGroup.asEntity(),
                                ),
                            ),
                        )
                    },
                )
            }
        },
        snackbarHostState = snackbarHostState,
    )
}

private fun LazyListScope.listenSection(
    releaseGroup: ReleaseGroupDetailsModel,
    filterText: String,
    now: Instant,
    collapsed: Boolean,
    onCollapseExpand: () -> Unit,
    onItemClick: MusicBrainzItemClickHandler,
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
    onSeeAllListensClick: () -> Unit,
) {
    if (releaseGroup.listenCount != null) {
        stickyHeader {
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.listens),
                collapsed = collapsed,
                onClick = onCollapseExpand,
                additionalContent = {
                    TextWithIcon(
                        imageVector = CustomIcons.Headphones,
                        text = releaseGroup.listenCount.toString(),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                },
            )
        }
        if (!collapsed) {
            items(releaseGroup.latestListens) { listen ->
                LastListenedListItemWithMoreActions(
                    listenedMs = listen.listenedMs,
                    recordingId = listen.id,
                    title = listen.getNameWithDisambiguation(),
                    filterText = filterText,
                    now = now,
                    onClick = { recordingId ->
                        onItemClick(
                            MusicBrainzEntityType.RECORDING,
                            recordingId,
                        )
                    },
                    onGoToListenAtEpochSeconds = onGoToListenAtEpochSeconds,
                )
            }
            item {
                ClickableItem(
                    title = stringResource(Res.string.seeAllListens),
                    filterText = filterText,
                    endIcon = CustomIcons.ChevronRight,
                    onClick = onSeeAllListensClick,
                )
            }
            item {
                UrlListItem(
                    relation = RelationListItemModel(
                        id = "listenbrainz_url",
                        type = "ListenBrainz",
                        linkedEntity = MusicBrainzEntityType.URL,
                        name = releaseGroup.listenBrainzUrl,
                        linkedEntityId = "listenbrainz_url",
                    ),
                    filterText = filterText,
                )
            }
        }
    }
}
