package ly.david.mbjc.ui.label

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.screen.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.label.details.LabelDetailsScreen
import ly.david.mbjc.ui.label.releases.ReleasesByLabelScreen
import ly.david.mbjc.ui.label.stats.LabelStatsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LabelScaffold(
    labelId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: () -> Unit = {},
    viewModel: LabelScaffoldViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.LABEL
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by rememberSaveable { mutableStateOf(LabelTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val label by viewModel.label.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = labelId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.onSelectedTabChange(
            labelId = labelId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                resource = resource,
                title = title,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.LABEL, resourceId = labelId)
                    CopyToClipboardMenuItem(labelId)
                    AddToCollectionMenuItem(onClick = onAddToCollectionMenuClick)
                },
                tabsTitles = LabelTab.values().map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = LabelTab.values()[it] },
                showFilterIcon = selectedTab == LabelTab.RELEASES,
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val releasesLazyListState = rememberLazyListState()
        var pagedReleasesFlow: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
            rememberFlowWithLifecycleStarted(pagedReleasesFlow)
                .collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            LabelTab.DETAILS -> {
                DetailsWithErrorHandling(
                    modifier = Modifier.padding(innerPadding),
                    showError = showError,
                    onRetryClick = { forceRefresh = true },
                    scaffoldModel = label
                ) {
                    LabelDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        label = it,
                        lazyListState = detailsLazyListState
                    )
                }
            }
            LabelTab.RELEASES -> {
                ReleasesByLabelScreen(
                    labelId = labelId,
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    releasesLazyListState = releasesLazyListState,
                    releasesLazyPagingItems = releasesLazyPagingItems,
                    onPagedReleasesFlowChange = { pagedReleasesFlow = it },
                    onReleaseClick = onItemClick,
                    filterText = filterText
                )
            }
            LabelTab.RELATIONSHIPS -> {
                viewModel.loadRelations(labelId)

                RelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    onItemClick = onItemClick,
                    snackbarHostState = snackbarHostState,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            LabelTab.STATS -> {
                LabelStatsScreen(
                    labelId = labelId,
                    modifier = Modifier.padding(innerPadding),
                    tabs = LabelTab.values().map { it.tab }
                )
            }
        }
    }
}
