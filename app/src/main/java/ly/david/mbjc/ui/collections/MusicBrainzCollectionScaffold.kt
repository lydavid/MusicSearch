package ly.david.mbjc.ui.collections

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import ly.david.data.domain.AreaListItemModel
import ly.david.data.domain.ArtistListItemModel
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.EventListItemModel
import ly.david.data.domain.InstrumentListItemModel
import ly.david.data.domain.LabelListItemModel
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.RecordingListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.domain.SeriesListItemModel
import ly.david.data.domain.WorkListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.collections.areas.AreasByCollectionScreen
import ly.david.mbjc.ui.collections.artists.ArtistsByCollectionScreen
import ly.david.mbjc.ui.collections.events.EventsByCollectionScreen
import ly.david.mbjc.ui.collections.instruments.InstrumentsByCollectionScreen
import ly.david.mbjc.ui.collections.labels.LabelsByCollectionScreen
import ly.david.mbjc.ui.collections.places.PlacesByCollectionScreen
import ly.david.mbjc.ui.collections.recordings.RecordingsByCollectionScreen
import ly.david.mbjc.ui.collections.releasegroups.ReleaseGroupsByCollectionScreen
import ly.david.mbjc.ui.collections.releases.ReleasesByCollectionScreen
import ly.david.mbjc.ui.collections.series.SeriesByCollectionScreen
import ly.david.mbjc.ui.collections.works.WorksByCollectionScreen
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

    val areasLazyListState = rememberLazyListState()
    var pagedAreasFlow: Flow<PagingData<AreaListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val areasLazyPagingItems: LazyPagingItems<AreaListItemModel> =
        rememberFlowWithLifecycleStarted(pagedAreasFlow)
            .collectAsLazyPagingItems()

    val artistsLazyListState = rememberLazyListState()
    var pagedArtistsFlow: Flow<PagingData<ArtistListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val artistsLazyPagingItems: LazyPagingItems<ArtistListItemModel> =
        rememberFlowWithLifecycleStarted(pagedArtistsFlow)
            .collectAsLazyPagingItems()

    val eventsLazyListState = rememberLazyListState()
    var pagedEventsFlow: Flow<PagingData<EventListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val eventsLazyPagingItems: LazyPagingItems<EventListItemModel> =
        rememberFlowWithLifecycleStarted(pagedEventsFlow)
            .collectAsLazyPagingItems()

    val instrumentsLazyListState = rememberLazyListState()
    var pagedInstrumentsFlow: Flow<PagingData<InstrumentListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val instrumentsLazyPagingItems: LazyPagingItems<InstrumentListItemModel> =
        rememberFlowWithLifecycleStarted(pagedInstrumentsFlow)
            .collectAsLazyPagingItems()

    val labelsLazyListState = rememberLazyListState()
    var pagedLabelsFlow: Flow<PagingData<LabelListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val labelsLazyPagingItems: LazyPagingItems<LabelListItemModel> =
        rememberFlowWithLifecycleStarted(pagedLabelsFlow)
            .collectAsLazyPagingItems()

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

    val seriesLazyListState = rememberLazyListState()
    var pagedSeriesFlow: Flow<PagingData<SeriesListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val seriesLazyPagingItems: LazyPagingItems<SeriesListItemModel> =
        rememberFlowWithLifecycleStarted(pagedSeriesFlow)
            .collectAsLazyPagingItems()

    val worksLazyListState = rememberLazyListState()
    var pagedWorksFlow: Flow<PagingData<WorkListItemModel>> by remember { mutableStateOf(emptyFlow()) }
    val worksLazyPagingItems: LazyPagingItems<WorkListItemModel> =
        rememberFlowWithLifecycleStarted(pagedWorksFlow)
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
            MusicBrainzResource.AREA -> {
                AreasByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    snackbarHostState = snackbarHostState,
                    lazyListState = areasLazyListState,
                    lazyPagingItems = areasLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedAreasFlowChange = { pagedAreasFlow = it },
                    onAreaClick = onItemClick
                )
            }
            MusicBrainzResource.ARTIST -> {
                ArtistsByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    snackbarHostState = snackbarHostState,
                    lazyListState = artistsLazyListState,
                    lazyPagingItems = artistsLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedArtistsFlowChange = { pagedArtistsFlow = it },
                    onArtistClick = onItemClick
                )
            }
            MusicBrainzResource.EVENT -> {
                EventsByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    snackbarHostState = snackbarHostState,
                    lazyListState = eventsLazyListState,
                    lazyPagingItems = eventsLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedEventsFlowChange = { pagedEventsFlow = it },
                    onEventClick = onItemClick
                )
            }
            MusicBrainzResource.INSTRUMENT -> {
                InstrumentsByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    snackbarHostState = snackbarHostState,
                    lazyListState = instrumentsLazyListState,
                    lazyPagingItems = instrumentsLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedInstrumentsFlowChange = { pagedInstrumentsFlow = it },
                    onInstrumentClick = onItemClick
                )
            }
            MusicBrainzResource.LABEL -> {
                LabelsByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    snackbarHostState = snackbarHostState,
                    lazyListState = labelsLazyListState,
                    lazyPagingItems = labelsLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedLabelsFlowChange = { pagedLabelsFlow = it },
                    onLabelClick = onItemClick
                )
            }
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
            MusicBrainzResource.SERIES -> {
                SeriesByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    snackbarHostState = snackbarHostState,
                    lazyListState = seriesLazyListState,
                    lazyPagingItems = seriesLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedSeriesFlowChange = { pagedSeriesFlow = it },
                    onSeriesClick = onItemClick
                )
            }
            MusicBrainzResource.WORK -> {
                WorksByCollectionScreen(
                    collectionId = collectionId,
                    filterText = filterText,
                    snackbarHostState = snackbarHostState,
                    lazyListState = worksLazyListState,
                    lazyPagingItems = worksLazyPagingItems,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    onPagedWorksFlowChange = { pagedWorksFlow = it },
                    onWorkClick = onItemClick
                )
            }
            MusicBrainzResource.COLLECTION,
            MusicBrainzResource.GENRE,
            MusicBrainzResource.URL -> {
                error("Collections by ${entity?.name} not supported.")
            }
            null -> {
                FullScreenLoadingIndicator()
            }
        }
    }
}
