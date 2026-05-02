package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.shared.domain.common.UNKNOWN_TIME
import ly.david.musicsearch.shared.domain.common.getDateTimeFormatted
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listen.ListenWithTrack
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.getNumberOfFilteredItems
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.icons.StarFilled
import ly.david.musicsearch.ui.common.label.LabelListItem
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.formatPeriod
import ly.david.musicsearch.ui.common.relation.UrlListItem
import ly.david.musicsearch.ui.common.release.getDisplayString
import ly.david.musicsearch.ui.common.releasegroup.getDisplayString
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
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
internal fun ReleaseDetailsTabUi(
    release: ReleaseDetailsModel,
    detailsTabUiState: DetailsTabUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onCollapseExpandReleaseEvents: () -> Unit = {},
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
    onSeeAllListensClick: () -> Unit = {},
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
) {
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
    }
    val additionalDetailsSection: @Composable ReleaseDetailsModel.() -> Unit = {
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
            )

            releaseEventsSection(
                collapsed = detailsTabUiState.isReleaseEventsCollapsed,
                areas = areas,
                filterText = filterText,
                onCollapseExpandReleaseEvents = onCollapseExpandReleaseEvents,
                onItemClick = onItemClick,
            )

            listenSection(
                release = this@run,
                now = detailsTabUiState.now,
                onSeeAllListensClick = onSeeAllListensClick,
            )
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
        onCollapseExpandAliases = onCollapseExpandAliases,
    )
}

private fun LazyListScope.releaseLabelsSection(
    labels: ImmutableList<LabelListItemModel>,
    filterText: String,
    onItemClick: MusicBrainzItemClickHandler,
) {
    val filteredLabels = labels.filter { label ->
        val searchText = filterText.lowercase()
        listOf(
            label.getNameWithDisambiguation(),
            label.type,
            label.labelCode.toString(),
            label.catalogNumbers,
        ).any { it?.lowercase()?.contains(searchText) == true }
    }.toPersistentList()
    labels.ifNotNullOrEmpty {
        item {
            val numberOfFilteredItems = getNumberOfFilteredItems(
                filteredCount = filteredLabels.size,
                total = labels.size,
            )
            ListSeparatorHeader(stringResource(Res.string.labels) + " $numberOfFilteredItems")
        }
    }
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

private fun LazyListScope.releaseEventsSection(
    collapsed: Boolean,
    areas: ImmutableList<AreaListItemModel>,
    filterText: String,
    onCollapseExpandReleaseEvents: () -> Unit,
    onItemClick: MusicBrainzItemClickHandler,
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
                onClick = onCollapseExpandReleaseEvents,
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
    now: Instant,
    onSeeAllListensClick: () -> Unit,
) {
    if (release.listenCount != null) {
        item {
            ListSeparatorHeader(stringResource(Res.string.listens))
        }
        item {
            ListItem(
                headlineContent = {
                    Row {
                        val listenCount = release.listenCount.toString()
                        TextWithIcon(
                            imageVector = CustomIcons.Headphones,
                            text = listenCount,
                            contentDescription = "$listenCount ${stringResource(Res.string.listens)}",
                        )
                        if (release.completeListenCount > 0) {
                            val completeListenCount = release.completeListenCount.toString()
                            TextWithIcon(
                                imageVector = CustomIcons.StarFilled,
                                text = completeListenCount,
                                contentDescription = "$completeListenCount complete listens",
                                iconTint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                    }
                },
            )
        }
        items(release.latestListens) {
            LastListenedListItem(
                listen = it,
                now = now,
            )
        }
        item {
            ClickableItem(
                title = stringResource(Res.string.seeAllListens),
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
                filterText = "",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LastListenedListItem(
    listen: ListenWithTrack,
    now: Instant,
) {
    val instant = Instant.fromEpochMilliseconds(listen.listenedMs)
    val formattedDateTimePeriod = formatPeriod(instant.getDateTimePeriod(now = now))
    val formattedDateTime = instant.getDateTimeFormatted()

    val text = buildString {
        append(formattedDateTimePeriod)
        append(" ($formattedDateTime)")
        append(" - ")
        append(listen.mediumPosition)
        append(".")
        append(listen.trackNumber)
        append(". ")
        append(listen.trackName)
    }
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}
