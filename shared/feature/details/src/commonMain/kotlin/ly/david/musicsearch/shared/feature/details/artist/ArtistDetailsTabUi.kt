package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.artist.ArtistType
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.details.asEntity
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.feature.details.area.AreaSection
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.LastListenedListItemWithMoreActions
import ly.david.musicsearch.ui.common.artist.getDisplayString
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.relation.UrlListItem
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.text.TextWithIcon
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.born
import musicsearch.ui.common.generated.resources.created
import musicsearch.ui.common.generated.resources.date
import musicsearch.ui.common.generated.resources.died
import musicsearch.ui.common.generated.resources.dissolved
import musicsearch.ui.common.generated.resources.founded
import musicsearch.ui.common.generated.resources.gender
import musicsearch.ui.common.generated.resources.ipi
import musicsearch.ui.common.generated.resources.isni
import musicsearch.ui.common.generated.resources.listens
import musicsearch.ui.common.generated.resources.seeAllListens
import musicsearch.ui.common.generated.resources.sortName
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
internal fun ArtistDetailsTabUi(
    artist: ArtistDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onItemClick: MusicBrainzItemClickHandler,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
) {
    val eventSink = detailsTabUiState.eventSink

    DetailsTabUi(
        detailsModel = artist,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        entityInfoSection = {
            ArtistInformationSection(
                filterText = filterText,
            )
        },
        bringYourOwnLabelsSection = {
            artist.run {
                item {
                    AreaSection(
                        areaListItemModel = areaListItemModel,
                        filterText = filterText,
                        onItemClick = onItemClick,
                    )
                }

                // TODO: begin area, end area

                listenSection(
                    artist = this@run,
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
                                    entityFacet = artist.asEntity(),
                                ),
                            ),
                        )
                    },
                )
            }
        },
        snackbarHostState = snackbarHostState,
        onLoginClick = onLoginClick,
    )
}

@Composable
private fun ArtistDetailsModel.ArtistInformationSection(
    filterText: String = "",
) {
    sortName.ifNotEmpty {
        TextWithHeading(
            heading = stringResource(Res.string.sortName),
            text = it,
            filterText = filterText,
        )
    }
    type?.getDisplayString()?.let {
        TextWithHeading(
            heading = stringResource(Res.string.type),
            text = it,
            filterText = filterText,
        )
    }
    gender?.getDisplayString()?.let {
        TextWithHeading(
            heading = stringResource(Res.string.gender),
            text = it,
            filterText = filterText,
        )
    }
    LifeSpanText(
        lifeSpan = lifeSpan,
        heading = stringResource(Res.string.date),
        beginHeading = when (type) {
            ArtistType.PERSON -> stringResource(Res.string.born)
            ArtistType.CHARACTER -> stringResource(Res.string.created)
            else -> stringResource(Res.string.founded)
        },
        endHeading = when (type) {
            ArtistType.PERSON -> stringResource(Res.string.died)
            // Characters do not "die": https://musicbrainz.org/doc/Artist
            else -> stringResource(Res.string.dissolved)
        },
        filterText = filterText,
    )

    ipis.ifNotEmpty {
        TextWithHeading(
            heading = stringResource(Res.string.ipi),
            text = it.joinToString(", "),
            filterText = filterText,
        )
    }

    isnis.ifNotEmpty {
        TextWithHeading(
            heading = stringResource(Res.string.isni),
            text = it.joinToString(", "),
            filterText = filterText,
        )
    }
}

private fun LazyListScope.listenSection(
    artist: ArtistDetailsModel,
    filterText: String,
    now: Instant,
    collapsed: Boolean,
    onCollapseExpand: () -> Unit,
    onItemClick: MusicBrainzItemClickHandler,
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
    onSeeAllListensClick: () -> Unit,
) {
    if (artist.listenCount != null) {
        stickyHeader {
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.listens),
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
        if (!collapsed) {
            listensSectionContent(
                artist = artist,
                filterText = filterText,
                now = now,
                onSeeAllListensClick = onSeeAllListensClick,
                onItemClick = onItemClick,
                onGoToListenAtEpochSeconds = onGoToListenAtEpochSeconds,
            )
        }
    }
}

private fun LazyListScope.listensSectionContent(
    artist: ArtistDetailsModel,
    filterText: String,
    now: Instant,
    onSeeAllListensClick: () -> Unit,
    onItemClick: MusicBrainzItemClickHandler,
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
) {
    item {
        ListItem(
            headlineContent = {
                TextWithIcon(
                    imageVector = CustomIcons.Headphones,
                    text = artist.listenCount.toString(),
                )
            },
        )
    }
    items(artist.latestListens) { listen ->
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
                name = artist.listenBrainzUrl,
                linkedEntityId = "listenbrainz_url",
            ),
            filterText = filterText,
        )
    }
}
