package ly.david.mbjc.ui.collections

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.collections.places.PlacesByCollectionScreen
import ly.david.mbjc.ui.collections.recordings.RecordingsByCollectionScreen
import ly.david.mbjc.ui.collections.releasegroups.ReleaseGroupsByCollectionScreen
import ly.david.mbjc.ui.collections.releases.ReleasesByCollectionScreen
import ly.david.mbjc.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.ToggleMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter

/**
 * A single MusicBrainz collection.
 * Displays all items in this collection.
 *
 * User must be authenticated to view non-cached private collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MusicBrainzCollectionScaffold(
    collectionId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    sortReleaseGroupListItems: Boolean = false,
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {},
    viewModel: MusicBrainzCollectionViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    var collection: CollectionListItemModel? by remember { mutableStateOf(null) }
    var entity: MusicBrainzResource? by rememberSaveable { mutableStateOf(null) }
    var filterText by rememberSaveable { mutableStateOf("") }

    val placesLazyListState = rememberLazyListState()
    var pagedPlacesFlow: Flow<PagingData<PlaceListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val placesLazyPagingItems: LazyPagingItems<PlaceListItemModel> =
        rememberFlowWithLifecycleStarted(pagedPlacesFlow)
            .collectAsLazyPagingItems()

    val recordingsLazyListState = rememberLazyListState()
    var pagedRecordingsFlow: Flow<PagingData<RecordingListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val recordingsLazyPagingItems: LazyPagingItems<RecordingListItemModel> =
        rememberFlowWithLifecycleStarted(pagedRecordingsFlow)
            .collectAsLazyPagingItems()

    val releasesLazyListState = rememberLazyListState()
    var pagedReleasesFlow: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
        rememberFlowWithLifecycleStarted(pagedReleasesFlow)
            .collectAsLazyPagingItems()

    val releaseGroupsLazyListState = rememberLazyListState()
    var pagedReleaseGroups: Flow<PagingData<ListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val releaseGroupsLazyPagingItems = rememberFlowWithLifecycleStarted(pagedReleaseGroups)
        .collectAsLazyPagingItems()

    LaunchedEffect(key1 = collectionId) {
        collection = viewModel.getCollection(collectionId)
        entity = collection?.entity
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                onBack = onBack,
                title = collection?.name.orEmpty(),
                scrollBehavior = scrollBehavior,
                showFilterIcon = true,
                overflowDropdownMenuItems = {
                    CopyToClipboardMenuItem(collectionId)
                    if (entity == MusicBrainzResource.RELEASE_GROUP) {
                        ToggleMenuItem(
                            toggleOnText = R.string.sort,
                            toggleOffText = R.string.unsort,
                            toggled = sortReleaseGroupListItems,
                            onToggle = onSortReleaseGroupListItemsChange
                        )
                    }
                    if (entity == MusicBrainzResource.RELEASE) {
                        ToggleMenuItem(
                            toggleOnText = R.string.show_more_info,
                            toggleOffText = R.string.show_less_info,
                            toggled = showMoreInfoInReleaseListItem,
                            onToggle = onShowMoreInfoInReleaseListItemChange
                        )
                    }
                },
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        when (entity) {
            MusicBrainzResource.PLACE -> {
                PlacesByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    snackbarHostState = snackbarHostState,
                    lazyListState = placesLazyListState,
                    lazyPagingItems = placesLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedPlacesFlowChange = { pagedPlacesFlow = it },
                    onPlaceClick = onItemClick
                )
            }
            MusicBrainzResource.RECORDING -> {
                RecordingsByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    snackbarHostState = snackbarHostState,
                    lazyListState = recordingsLazyListState,
                    lazyPagingItems = recordingsLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedRecordingsFlowChange = { pagedRecordingsFlow = it },
                    onRecordingClick = onItemClick
                )
            }
            MusicBrainzResource.RELEASE -> {
                ReleasesByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    showMoreInfo = showMoreInfoInReleaseListItem,
                    snackbarHostState = snackbarHostState,
                    lazyListState = releasesLazyListState,
                    lazyPagingItems = releasesLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedReleasesFlowChange = { pagedReleasesFlow = it },
                    onReleaseClick = onItemClick
                )
            }
            MusicBrainzResource.RELEASE_GROUP -> {
                ReleaseGroupsByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    isSorted = sortReleaseGroupListItems,
                    snackbarHostState = snackbarHostState,
                    lazyListState = releaseGroupsLazyListState,
                    lazyPagingItems = releaseGroupsLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onReleaseGroupClick = onItemClick,
                    onPagedReleaseGroupsChange = {
                        pagedReleaseGroups = it
                    }
                )
            }
            null -> {
                FullScreenLoadingIndicator()
            }
            else -> {
                Text(text = collectionId)
            }
        }
    }
}
