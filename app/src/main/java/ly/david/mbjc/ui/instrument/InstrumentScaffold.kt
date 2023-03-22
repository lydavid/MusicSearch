package ly.david.mbjc.ui.instrument

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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.ListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.screen.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.AddToCollectionMenuItem
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.instrument.details.InstrumentDetailsScreen
import ly.david.mbjc.ui.instrument.stats.InstrumentStatsScreen

/**
 * The top-level screen for an instrument.
 *
 * All of its content are relationships, there's no browsing supported in the API.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstrumentScaffold(
    instrumentId: String,
    modifier: Modifier = Modifier,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onAddToCollectionMenuClick: () -> Unit = {},
    viewModel: InstrumentScaffoldViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.INSTRUMENT
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by rememberSaveable { mutableStateOf(InstrumentTab.DETAILS) }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val instrument by viewModel.instrument.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = instrumentId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.onSelectedTabChange(
            instrumentId = instrumentId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                resource = resource,
                title = title,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = resource, resourceId = instrumentId)
                    CopyToClipboardMenuItem(instrumentId)
                    AddToCollectionMenuItem(onClick = onAddToCollectionMenuClick)
                },
                tabsTitles = InstrumentTab.values().map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = InstrumentTab.values()[it] },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            InstrumentTab.DETAILS -> {
                DetailsWithErrorHandling(
                    modifier = Modifier.padding(innerPadding),
                    showError = showError,
                    onRetryClick = { forceRefresh = true },
                    scaffoldModel = instrument
                ) {
                    InstrumentDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        instrument = it,
                        lazyListState = detailsLazyListState
                    )
                }
            }
            InstrumentTab.RELATIONSHIPS -> {
                viewModel.loadRelations(instrumentId)

                RelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            InstrumentTab.STATS -> {
                InstrumentStatsScreen(
                    instrumentId = instrumentId,
                    modifier = Modifier.padding(innerPadding),
                    tabs = InstrumentTab.values().map { it.tab }
                )
            }
        }
    }
}
