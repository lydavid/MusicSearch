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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.data.showReleases
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.details.AreaDetailsScreen
import ly.david.mbjc.ui.area.places.PlacesByAreaScreen
import ly.david.mbjc.ui.area.releases.ReleasesByAreaScreen
import ly.david.mbjc.ui.area.stats.AreaStatsScreen
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter

internal enum class AreaTab(@StringRes val titleRes: Int) {
    DETAILS(R.string.details),
    RELATIONSHIPS(R.string.relationships),
    RELEASES(R.string.releases),
    PLACES(R.string.places),
    STATS(R.string.stats)
}

/**
 * The top-level screen for an area.
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
    var selectedTab by rememberSaveable { mutableStateOf(AreaTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val area by viewModel.area.collectAsState()
    val tabs by viewModel.tabs.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = areaId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.onSelectedTabChange(
            areaId = areaId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
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
                showFilterIcon = selectedTab in listOf(AreaTab.RELEASES, AreaTab.PLACES),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        val releasesLazyListState = rememberLazyListState()
        var pagedReleasesFlow: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
            rememberFlowWithLifecycleStarted(pagedReleasesFlow)
                .collectAsLazyPagingItems()

        val placesLazyListState = rememberLazyListState()
        var pagedPlacesFlow: Flow<PagingData<PlaceListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val placesLazyPagingItems: LazyPagingItems<PlaceListItemModel> =
            rememberFlowWithLifecycleStarted(pagedPlacesFlow)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            AreaTab.DETAILS -> {
                DetailsWithErrorHandling(
                    showError = showError,
                    onRetryClick = { forceRefresh = true },
                    scaffoldModel = area
                ) {
                    AreaDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        area = it,
                        lazyListState = detailsLazyListState
                    )
                }
            }
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
                ReleasesByAreaScreen(
                    areaId = areaId,
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    releasesLazyListState = releasesLazyListState,
                    releasesLazyPagingItems = releasesLazyPagingItems,
                    onPagedReleasesFlowChange = { pagedReleasesFlow = it },
                    onReleaseClick = onItemClick,
                    filterText = filterText
                )
            }
            AreaTab.PLACES -> {
                PlacesByAreaScreen(
                    areaId = areaId,
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    placesLazyListState = placesLazyListState,
                    placesLazyPagingItems = placesLazyPagingItems,
                    onPagedPlacesFlowChange = { pagedPlacesFlow = it },
                    onPlaceClick = onItemClick,
                    filterText = filterText
                )
            }
            AreaTab.STATS -> {
                AreaStatsScreen(
                    areaId = areaId,
                    showReleases = area?.showReleases() ?: false
                )
            }
        }
    }
}
