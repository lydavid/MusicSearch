package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.LastListenedListItem
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.icons.MusicVideo
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.relation.UrlListItem
import ly.david.musicsearch.ui.common.screen.NavigatableFromOverlayResult
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.text.TextWithIcon
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.firstReleaseDate
import musicsearch.ui.common.generated.resources.isrc
import musicsearch.ui.common.generated.resources.length
import musicsearch.ui.common.generated.resources.listens
import musicsearch.ui.common.generated.resources.seeAllListens
import musicsearch.ui.common.generated.resources.video
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
internal fun RecordingDetailsTabUi(
    recording: RecordingDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onSeeAllListensClick: () -> Unit,
    onCollapseExpandSection: (CollapsibleSection) -> Unit = {},
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
    snackbarHostState: SnackbarHostState,
    onGoToScreen: (screen: NavigatableFromOverlayResult) -> Unit,
    onRefreshLocal: () -> Unit,
    onLoginClick: () -> Unit,
) {
    DetailsTabUi(
        detailsModel = recording,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandSection = onCollapseExpandSection,
        snackbarHostState = snackbarHostState,
        onGoToScreen = onGoToScreen,
        onRefreshLocal = onRefreshLocal,
        onLoginClick = onLoginClick,
        entityInfoSection = {
            if (video) {
                TextWithIcon(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 4.dp,
                    ),
                    imageVector = CustomIcons.MusicVideo,
                    text = stringResource(Res.string.video),
                )
            }
            length?.ifNotNull {
                TextWithHeading(
                    heading = stringResource(Res.string.length),
                    text = it.toDisplayTime(),
                    filterText = filterText,
                )
            }
            firstReleaseDate.ifNotEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.firstReleaseDate),
                    text = it,
                    filterText = filterText,
                )
            }
            isrcs.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.isrc),
                    text = it.joinToString(", "),
                    filterText = filterText,
                )
            }
        },
        bringYourOwnLabelsSection = {
            listenSection(
                recording = recording,
                filterText = filterText,
                now = detailsTabUiState.now,
                collapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.Listens),
                onCollapseExpand = { onCollapseExpandSection(CollapsibleSection.Listens) },
                onGoToListenAtEpochSeconds = onGoToListenAtEpochSeconds,
                onSeeAllListensClick = onSeeAllListensClick,
            )
        },
    )
}

private fun LazyListScope.listenSection(
    recording: RecordingDetailsModel,
    filterText: String,
    now: Instant,
    collapsed: Boolean,
    onCollapseExpand: () -> Unit,
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
    onSeeAllListensClick: () -> Unit,
) {
    if (recording.listenCount != null) {
        stickyHeader {
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.listens),
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
        if (!collapsed) {
            item {
                ListItem(
                    headlineContent = {
                        TextWithIcon(
                            imageVector = CustomIcons.Headphones,
                            text = recording.listenCount.toString(),
                        )
                    },
                )
            }
            items(recording.latestListensTimestampsMs) {
                LastListenedListItem(
                    lastListenedMs = it,
                    now = now,
                    filterText = filterText,
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
                        name = recording.listenBrainzUrl,
                        linkedEntityId = "listenbrainz_url",
                    ),
                    filterText = filterText,
                )
            }
        }
    }
}
