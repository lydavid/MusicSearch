package ly.david.mbjc.ui.event

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
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.Tab
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.event.details.EventDetailsScreen

internal enum class EventTab(val tab: Tab) {
    DETAILS(Tab.DETAILS),
    RELATIONSHIPS(Tab.RELATIONSHIPS),
    STATS(Tab.STATS)
}

/**
 * The top-level screen for an event.
 *
 * All of its content are relationships, there's no browsing supported in the API.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventScaffold(
    eventId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: EventScaffoldViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.EVENT
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by rememberSaveable { mutableStateOf(EventTab.DETAILS) }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val event by viewModel.event.collectAsState()
    val showError by viewModel.isError.collectAsState()

    LaunchedEffect(key1 = eventId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.onSelectedTabChange(
            eventId = eventId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = resource,
                title = title,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource, eventId)
                    CopyToClipboardMenuItem(eventId)
                },
                tabsTitles = EventTab.values().map { stringResource(id = it.tab.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = EventTab.values()[it] },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            EventTab.DETAILS -> {
                DetailsWithErrorHandling(
                    showError = showError,
                    onRetryClick = { forceRefresh = true },
                    scaffoldModel = event
                ) {
                    EventDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        event = it,
                        lazyListState = detailsLazyListState
                    )
                }
            }
            EventTab.RELATIONSHIPS -> {
                viewModel.loadRelations(eventId)

                RelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            EventTab.STATS -> {

            }
        }
    }
}
