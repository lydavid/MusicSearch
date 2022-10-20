package ly.david.mbjc.ui.area

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.R
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.AreaType
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.area.relations.AreaRelationsScreen
import ly.david.mbjc.ui.area.stats.AreaStatsScreen
import ly.david.mbjc.ui.common.paging.ReleasesListScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch
import ly.david.mbjc.ui.navigation.Destination

// TODO: one way to deal with massive number of relationships is
//  to split them into different tabs
//  asking for release-rels and recording-rels in a separate tab for area for example
//  however, we would then need a different indicator to determine whether we've fetched these types of rels
private enum class AreaTab(@StringRes val titleRes: Int) {
    RELATIONSHIPS(R.string.relationships),
    RELEASES(R.string.releases),

    //    RELATIONSHIPS_RELEASES(R.string.relationships_releases),
//    RELATIONSHIPS_RECORDINGS(R.string.relationships_recordings),
    STATS(R.string.stats)
}

/**
 * A screen that starts on relationships tab.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AreaScaffold(
    areaId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: AreaViewModel = hiltViewModel(),
) {

    val resource = MusicBrainzResource.AREA

    val snackbarHostState = remember { SnackbarHostState() }

    // TODO: api doesn't seem to include area containment
    //  but we could get its parent area via relations "part of" "backward"
    var area: Area? by remember { mutableStateOf(null) }
    var title by rememberSaveable { mutableStateOf("") }
    val tabs = AreaTab.values()
        .filter { it != AreaTab.RELEASES || area?.type == AreaType.COUNTRY }
    var selectedTab by rememberSaveable { mutableStateOf(AreaTab.RELATIONSHIPS) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }

    val relationsLazyListState = rememberLazyListState()
    val relationsLazyPagingItems: LazyPagingItems<UiModel> = rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
        .collectAsLazyPagingItems()

    val releasesLazyListState = rememberLazyListState()
    val releasesLazyPagingItems: LazyPagingItems<ReleaseUiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedReleases)
            .collectAsLazyPagingItems()

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        title = titleWithDisambiguation
    }

    LaunchedEffect(key1 = areaId) {

        try {
            val areaUiModel = viewModel.lookupAreaThenLoadRelations(areaId)
            if (titleWithDisambiguation.isNullOrEmpty()) {
                title = areaUiModel.getNameWithDisambiguation()
            }
            area = areaUiModel
        } catch (ex: Exception) {
            viewModel.loadRelations(areaId)
        }

        if (!recordedLookup) {
            viewModel.recordLookupHistory(
                resourceId = areaId,
                resource = resource,
                summary = title
            )
            recordedLookup = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithSearch(
                resource = resource,
                title = title,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.AREA, resourceId = areaId)
                },
                tabsTitles = tabs.map { stringResource(id = it.titleRes) },
                selectedTabIndex = tabs.indexOf(selectedTab),
                onSelectTabIndex = { selectedTab = tabs[it] },
                showSearchIcon = selectedTab == AreaTab.RELEASES,
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                    viewModel.updateQuery(searchText)
                },
            )
        },
    ) { innerPadding ->

        when (selectedTab) {
            AreaTab.RELATIONSHIPS -> {
                AreaRelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            AreaTab.RELEASES -> {
                viewModel.loadReleases(areaId)

                ReleasesListScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    lazyListState = releasesLazyListState,
                    lazyPagingItems = releasesLazyPagingItems,
                    onReleaseClick = { id, title ->
                        onItemClick(Destination.LOOKUP_RELEASE, id, title)
                    }
                )
            }
//            AreaTab.RELATIONSHIPS_RELEASES -> {
//            }
//            AreaTab.RELATIONSHIPS_RECORDINGS -> {
//
//            }
            AreaTab.STATS -> {
                AreaStatsScreen(
                    areaId = areaId,
                    showReleases = area?.type == AreaType.COUNTRY
                )
            }
        }
    }
}
