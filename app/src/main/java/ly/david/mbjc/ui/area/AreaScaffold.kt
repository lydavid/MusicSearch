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
import ly.david.data.domain.AreaUiModel
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.UiModel
import ly.david.data.domain.showReleases
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.stats.AreaStatsScreen
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.paging.ReleasesListScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch

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
@Composable
internal fun AreaScaffold(
    areaId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: AreaViewModel = hiltViewModel(),
) {
    val resource = MusicBrainzResource.AREA

    // TODO: api doesn't seem to include area containment
    //  but we could get its parent area via relations "part of" "backward"
    var area: AreaUiModel? by remember { mutableStateOf(null) }
    var title by rememberSaveable { mutableStateOf("") }
    var tabs: List<AreaTab> by rememberSaveable { mutableStateOf(AreaTab.values().filter { it != AreaTab.RELEASES }) }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }

    val releasesLazyPagingItems: LazyPagingItems<ReleaseUiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedReleases)
            .collectAsLazyPagingItems()

    val relationsLazyPagingItems: LazyPagingItems<UiModel> = rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
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
            if (areaUiModel.showReleases()) {
                tabs = AreaTab.values().toList()
            }
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

    AreaScaffold(
        areaId = areaId,
        onBack = onBack,
        onItemClick = onItemClick,
        releasesLazyPagingItems = releasesLazyPagingItems,
        relationsLazyPagingItems = relationsLazyPagingItems,
        resource = resource,
        title = title,
        tabs = tabs,
        showReleases = area?.showReleases() ?: false,
        onUpdateQuery = { searchText ->
            viewModel.updateQuery(searchText)
        },
        loadReleases = {
            viewModel.loadReleases(areaId)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AreaScaffold(
    areaId: String,
    onBack: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    resource: MusicBrainzResource,
    title: String,
    tabs: List<AreaTab>,
    releasesLazyPagingItems: LazyPagingItems<ReleaseUiModel>,
    relationsLazyPagingItems: LazyPagingItems<UiModel>,
    showReleases: Boolean,
    onUpdateQuery: (String) -> Unit,
    loadReleases: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTab by rememberSaveable { mutableStateOf(AreaTab.RELATIONSHIPS) }
    var searchText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithSearch(
                resource = resource,
                title = title,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.AREA, resourceId = areaId)
                    CopyToClipboardMenuItem(areaId)
                },
                tabsTitles = tabs.map { stringResource(id = it.titleRes) },
                selectedTabIndex = tabs.indexOf(selectedTab),
                onSelectTabIndex = { selectedTab = tabs[it] },
                showSearchIcon = selectedTab == AreaTab.RELEASES,
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                    onUpdateQuery(searchText)
                },
            )
        },
    ) { innerPadding ->

        val relationsLazyListState = rememberLazyListState()
        val releasesLazyListState = rememberLazyListState()

        when (selectedTab) {
            AreaTab.RELATIONSHIPS -> {
                RelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            AreaTab.RELEASES -> {
                loadReleases()

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
            AreaTab.STATS -> {
                AreaStatsScreen(
                    areaId = areaId,
                    showReleases = showReleases
                )
            }
        }
    }
}
