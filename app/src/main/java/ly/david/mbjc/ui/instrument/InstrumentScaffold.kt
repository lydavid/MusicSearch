package ly.david.mbjc.ui.instrument

import androidx.annotation.StringRes
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
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.instrument.details.InstrumentDetailsScreen

internal enum class InstrumentTab(@StringRes val titleRes: Int) {
    DETAILS(R.string.details),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats)
}

/**
 * The top-level screen for an instrument.
 *
 * All of its content are relationships, there's no browsing supported in the API.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstrumentScaffold(
    instrumentId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
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
        topBar = {
            ScrollableTopAppBar(
                resource = resource,
                title = title,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = resource, resourceId = instrumentId)
                    CopyToClipboardMenuItem(instrumentId)
                },
                tabsTitles = InstrumentTab.values().map { stringResource(id = it.titleRes) },
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

            }
        }
    }
}
