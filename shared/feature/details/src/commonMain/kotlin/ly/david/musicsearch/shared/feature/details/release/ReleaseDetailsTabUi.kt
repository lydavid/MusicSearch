package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.shared.domain.common.UNKNOWN_TIME
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.details.asEntity
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.LastListenedListItemWithMoreActions
import ly.david.musicsearch.shared.feature.details.utils.getNumberOfFilteredItems
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.icons.StarFilled
import ly.david.musicsearch.ui.common.label.LabelListItem
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.relation.UrlListItem
import ly.david.musicsearch.ui.common.release.getDisplayString
import ly.david.musicsearch.ui.common.releasegroup.getDisplayString
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.work.getDisplayLanguage
import ly.david.musicsearch.ui.common.work.getDisplayScript
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.asin
import musicsearch.ui.common.generated.resources.barcode
import musicsearch.ui.common.generated.resources.dataQuality
import musicsearch.ui.common.generated.resources.date
import musicsearch.ui.common.generated.resources.format
import musicsearch.ui.common.generated.resources.labels
import musicsearch.ui.common.generated.resources.language
import musicsearch.ui.common.generated.resources.length
import musicsearch.ui.common.generated.resources.listens
import musicsearch.ui.common.generated.resources.packaging
import musicsearch.ui.common.generated.resources.releaseEvents
import musicsearch.ui.common.generated.resources.script
import musicsearch.ui.common.generated.resources.seeAllListens
import musicsearch.ui.common.generated.resources.status
import musicsearch.ui.common.generated.resources.tracks
import musicsearch.ui.common.generated.resources.type
import musicsearch.ui.common.generated.resources.xCompleteListens
import musicsearch.ui.common.generated.resources.xListens
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
internal fun ReleaseDetailsTabUi(
    release: ReleaseDetailsModel,
    detailsTabUiState: DetailsTabUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onItemClick: MusicBrainzItemClickHandler,
    snackbarHostState: SnackbarHostState,
) {
    val eventSink = detailsTabUiState.eventSink

    val entityInfoSection: @Composable ReleaseDetailsModel.() -> Unit = {
        barcode.ifNotEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.barcode),
                text = it,
                filterText = filterText,
            )
        }
        formattedFormats.ifNotNullOrEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.format),
                text = it,
                filterText = filterText,
            )
        }
        formattedTracks.ifNotNullOrEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.tracks),
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
            heading = stringResource(Res.string.length),
            text = formattedReleaseLength,
            filterText = filterText,
        )

        date.ifNotNullOrEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.date),
                text = it,
                filterText = filterText,
            )
        }
        releaseGroup?.let {
            TextWithHeading(
                heading = stringResource(Res.string.type),
                text = it.getDisplayString(),
                filterText = filterText,
            )
        }
        packaging.ifNotEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.packaging),
                text = it,
                filterText = filterText,
            )
        }
        TextWithHeading(
            heading = stringResource(Res.string.status),
            text = status.getDisplayString(),
            filterText = filterText,
        )
        textRepresentation.language.getDisplayLanguage().ifNotNullOrEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.language),
                text = it,
                filterText = filterText,
            )
        }
        textRepresentation.script.getDisplayScript().ifNotNullOrEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.script),
                text = it,
                filterText = filterText,
            )
        }
        quality.ifNotEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.dataQuality),
                text = it,
                filterText = filterText,
            )
        }
        asin.ifNotEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.asin),
                text = it,
                filterText = filterText,
            )
        }
    }
    val bringYourOwnLabelsSection: LazyListScope.() -> Unit = {
        release.run {
            releaseLabelsSection(
                labels = labels,
                filterText = filterText,
                onItemClick = onItemClick,
                collapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.Labels),
                onCollapseExpand = {
                    eventSink(DetailsTabUiEvent.ToggleCollapseExpandSection(CollapsibleSection.Labels))
                },
            )

            releaseEventsSection(
                areas = areas,
                filterText = filterText,
                onItemClick = onItemClick,
                collapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.ReleaseEvents),
                onCollapseExpand = {
                    eventSink(DetailsTabUiEvent.ToggleCollapseExpandSection(CollapsibleSection.ReleaseEvents))
                },
            )

            listenSection(
                release = this@run,
                filterText = filterText,
                collapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.Listens),
                onCollapseExpand = {
                    eventSink(DetailsTabUiEvent.ToggleCollapseExpandSection(CollapsibleSection.Listens))
                },
                now = detailsTabUiState.now,
                onSeeAllListensClick = {
                    eventSink(
                        DetailsTabUiEvent.GoToScreen(
                            screen = ListensScreen(
                                entityFacet = release.asEntity(),
                            ),
                        ),
                    )
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
            )
        }
    }
    DetailsTabUi(
        detailsModel = release,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        entityInfoSection = entityInfoSection,
        bringYourOwnLabelsSection = bringYourOwnLabelsSection,
        snackbarHostState = snackbarHostState,
    )
}

private fun LazyListScope.releaseLabelsSection(
    labels: ImmutableList<LabelListItemModel>,
    filterText: String,
    onItemClick: MusicBrainzItemClickHandler,
    collapsed: Boolean,
    onCollapseExpand: () -> Unit,
) {
    val filteredLabels = labels.filter { label ->
        val searchText = filterText.lowercase()
        listOf(
            label.getNameWithDisambiguation(),
            label.type?.displayName,
            label.labelCode.toString(),
            label.catalogNumbers,
        ).any { it?.lowercase()?.contains(searchText) == true }
    }.toPersistentList()
    labels.ifNotNullOrEmpty {
        stickyHeader {
            val numberOfFilteredItems = getNumberOfFilteredItems(
                filteredCount = filteredLabels.size,
                total = labels.size,
            )
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.labels) + " $numberOfFilteredItems",
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
    }

    if (!collapsed) {
        items(filteredLabels) { label ->
            LabelListItem(
                label = label,
                filterText = filterText,
                onLabelClick = {
                    onItemClick(
                        MusicBrainzEntityType.LABEL,
                        id,
                    )
                },
                showIcon = false,
                showEditCollection = false,
            )
        }
    }
}

private fun LazyListScope.releaseEventsSection(
    areas: ImmutableList<AreaListItemModel>,
    filterText: String,
    onItemClick: MusicBrainzItemClickHandler,
    collapsed: Boolean,
    onCollapseExpand: () -> Unit,
) {
    val filteredAreas = areas.filter { area ->
        val searchText = filterText.lowercase()
        listOf(
            area.getNameWithDisambiguation(),
            area.date,
        ).any { it?.lowercase()?.contains(searchText) == true }
    }.toPersistentList()
    areas.ifNotNullOrEmpty {
        stickyHeader {
            val numberOfFilteredItems = getNumberOfFilteredItems(
                filteredCount = filteredAreas.size,
                total = areas.size,
            )
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.releaseEvents) + " $numberOfFilteredItems",
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
    }
    if (!collapsed) {
        items(filteredAreas) { area: AreaListItemModel ->
            AreaListItem(
                area = area,
                filterText = filterText,
                showType = false,
                showIcon = false,
                showEditCollection = false,
                onAreaClick = {
                    onItemClick(
                        MusicBrainzEntityType.AREA,
                        id,
                    )
                },
            )
        }
    }
}

private fun LazyListScope.listenSection(
    release: ReleaseDetailsModel,
    filterText: String,
    collapsed: Boolean,
    now: Instant,
    onCollapseExpand: () -> Unit,
    onSeeAllListensClick: () -> Unit,
    onItemClick: MusicBrainzItemClickHandler,
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
) {
    if (release.listenCount != null) {
        stickyHeader {
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.listens),
                collapsed = collapsed,
                onClick = onCollapseExpand,
                additionalContent = {
                    val listenCount = release.listenCount
                    TextWithIcon(
                        imageVector = CustomIcons.Headphones,
                        text = listenCount.toString(),
                        contentDescription = pluralStringResource(
                            Res.plurals.xListens,
                            listenCount?.toInt() ?: 0,
                            listenCount ?: 0,
                        ),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                    if (release.completeListenCount > 0) {
                        val completeListenCount = release.completeListenCount
                        TextWithIcon(
                            imageVector = CustomIcons.StarFilled,
                            text = completeListenCount.toString(),
                            contentDescription = pluralStringResource(
                                Res.plurals.xCompleteListens,
                                completeListenCount.toInt(),
                                completeListenCount,
                            ),
                            iconTint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                },
            )
        }
        if (!collapsed) {
            listensSectionContent(
                release = release,
                filterText = filterText,
                now = now,
                onSeeAllListensClick = onSeeAllListensClick,
                onItemClick = onItemClick,
                onGoToListenAtEpochSeconds = onGoToListenAtEpochSeconds,
            )
        }
    }
}

@Suppress("LongMethod")
private fun LazyListScope.listensSectionContent(
    release: ReleaseDetailsModel,
    filterText: String,
    now: Instant,
    onSeeAllListensClick: () -> Unit,
    onItemClick: MusicBrainzItemClickHandler,
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
) {
    items(release.latestListens) { listen ->
        LastListenedListItemWithMoreActions(
            listenedMs = listen.listenedMs,
            recordingId = listen.recordingId,
            title = "${listen.mediumPosition}.${listen.trackNumber} ${listen.trackName}",
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
                name = release.listenBrainzUrl,
                linkedEntityId = "listenbrainz_url",
            ),
            filterText = filterText,
        )
    }
}
