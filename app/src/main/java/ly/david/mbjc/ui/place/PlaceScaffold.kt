package ly.david.mbjc.ui.place

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.data.domain.EventListItemModel
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.place.details.PlaceDetailsScreen
import ly.david.mbjc.ui.place.events.EventsByPlaceScreen

private enum class PlaceTab(@StringRes val titleRes: Int) {
    DETAILS(R.string.details),

    // Should exclude event-rels because they appear to be the same as the results from browse events by place
    RELATIONSHIPS(R.string.relationships),
    EVENTS(R.string.events),
    STATS(R.string.stats)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlaceScaffold(
    placeId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: PlaceViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.PLACE

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var titleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(PlaceTab.DETAILS) }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var place: PlaceListItemModel? by remember { mutableStateOf(null) }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        titleState = titleWithDisambiguation
    }

    LaunchedEffect(key1 = placeId) {
        try {
            val placeListItemModel = viewModel.lookupPlace(placeId)
            if (titleWithDisambiguation.isNullOrEmpty()) {
                titleState = placeListItemModel.getNameWithDisambiguation()
            }
            place = placeListItemModel
        } catch (ex: Exception) {
            Log.d("Remove This", "PlaceScaffold: $ex")
        }

        if (!recordedLookup) {
            viewModel.recordLookupHistory(
                resourceId = placeId,
                resource = resource,
                summary = titleState
            )
            recordedLookup = true
        }
    }

    Scaffold(
        topBar = {
            // TODO: filter relationships
            TopAppBarWithFilter(
                resource = resource,
                title = titleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = resource, resourceId = placeId)
                    CopyToClipboardMenuItem(placeId)
                },
                tabsTitles = PlaceTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = PlaceTab.values()[it] },
                showFilterIcon = selectedTab == PlaceTab.EVENTS,
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

        val eventsLazyListState = rememberLazyListState()
        var pagedEventsFlow: Flow<PagingData<EventListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val eventsLazyPagingItems: LazyPagingItems<EventListItemModel> =
            rememberFlowWithLifecycleStarted(pagedEventsFlow)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            PlaceTab.DETAILS -> {
                val placeListItemModel = place
                if (placeListItemModel == null) {
                    FullScreenLoadingIndicator()
                } else {
                    PlaceDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        context = context,
                        place = placeListItemModel,
                        lazyListState = detailsLazyListState,
                        onItemClick = onItemClick
                    )
                }
            }
            PlaceTab.RELATIONSHIPS -> {
                viewModel.loadRelations(placeId)

                RelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            PlaceTab.EVENTS -> {
                EventsByPlaceScreen(
                    placeId = placeId,
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    eventsLazyListState = eventsLazyListState,
                    eventsLazyPagingItems = eventsLazyPagingItems,
                    onPagedEventsFlowChange = { pagedEventsFlow = it },
                    onEventClick = onItemClick,
                    filterText = filterText
                )
            }
            PlaceTab.STATS -> {

            }
        }
    }
}
