package ly.david.musicsearch.share.feature.database.all

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.EntitiesListUi
import ly.david.musicsearch.ui.common.getNamePlural
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.core.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllEntitiesUi(
    state: AllEntitiesUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val releasesEventSink = state.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.releaseGroupsListUiState.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(AllEntitiesUiEvent.NavigateUp)
                },
                title = state.entity.getNamePlural(strings),
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    if (state.entity == MusicBrainzEntity.RELEASE_GROUP) {
                        ToggleMenuItem(
                            toggleOnText = strings.sort,
                            toggleOffText = strings.unsort,
                            toggled = state.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (state.entity == MusicBrainzEntity.RELEASE) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.releasesListUiState.showMoreInfo,
                            onToggle = {
                                releasesEventSink(ReleasesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
                            },
                        )
                    }
                },
                topAppBarFilterState = state.topAppBarFilterState,
                topAppBarEditState = state.topAppBarEditState,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        EntitiesListUi(
            isEditMode = state.topAppBarEditState.isEditMode,
            areasListUiState = state.areasListUiState,
            artistsListUiState = state.artistsListUiState,
            eventsListUiState = state.eventsListUiState,
            genresListUiState = state.genresListUiState,
            instrumentsListUiState = state.instrumentsListUiState,
            labelsListUiState = state.labelsListUiState,
            placesListUiState = state.placesListUiState,
            recordingsListUiState = state.recordingsListUiState,
            releasesListUiState = state.releasesListUiState,
            releaseGroupsListUiState = state.releaseGroupsListUiState,
            seriesListUiState = state.seriesListUiState,
            worksListUiState = state.worksListUiState,
            entity = state.entity,
            innerPadding = innerPadding,
            scrollBehavior = scrollBehavior,
            onItemClick = { entity, id, title ->
                eventSink(
                    AllEntitiesUiEvent.ClickItem(
                        entity = entity,
                        id = id,
                        title = title,
                    ),
                )
            },
            requestForMissingCoverArtUrl = { entityId ->
                when (state.entity) {
                    MusicBrainzEntity.RELEASE -> {
                        releasesEventSink(ReleasesListUiEvent.RequestForMissingCoverArtUrl(entityId))
                    }

                    MusicBrainzEntity.RELEASE_GROUP -> {
                        releaseGroupsEventSink(ReleaseGroupsListUiEvent.RequestForMissingCoverArtUrl(entityId))
                    }

                    else -> {
                        // no-op
                    }
                }
            },
        )
    }
}
