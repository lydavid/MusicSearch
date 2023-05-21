package ly.david.mbjc.ui.collections

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import ly.david.data.domain.CollectionListItemModel
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
import ly.david.mbjc.ui.common.fullscreen.FullScreenText
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
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
internal fun CollectionScaffold(
    collectionId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    sortReleaseGroupListItems: Boolean = false,
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {},
    onDeleteFromCollection: (collectableId: String, name: String) -> Unit = { _, _ -> },
    viewModel: CollectionViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    var collection: CollectionListItemModel? by remember { mutableStateOf(null) }
    var entity: MusicBrainzResource? by rememberSaveable { mutableStateOf(null) }
    var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
    var filterText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = collectionId) {
        collection = viewModel.getCollection(collectionId)
        entity = collection?.entity
        isRemote = collection?.isRemote ?: false
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
                    if (collection?.isRemote == true) {
                        OpenInBrowserMenuItem(resource = MusicBrainzResource.COLLECTION, resourceId = collectionId)
                    }
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
        if (collection == null) {
            FullScreenText(
                text = "Cannot find collection.",
                modifier = Modifier
                    .padding(innerPadding)
            )
        } else {
            CollectionScaffoldContent(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                entity = entity,
                snackbarHostState = snackbarHostState,
                innerPadding = innerPadding,
                scrollBehavior = scrollBehavior,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                sortReleaseGroupListItems = sortReleaseGroupListItems,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectionScaffoldContent(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    entity: MusicBrainzResource?,
    snackbarHostState: SnackbarHostState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    showMoreInfoInReleaseListItem: Boolean = true,
    sortReleaseGroupListItems: Boolean = false,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (collectableId: String, name: String) -> Unit = { _, _ -> },
) {
    when (entity) {
        MusicBrainzResource.AREA -> {
            AreasByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onAreaClick = onItemClick
            )
        }

        MusicBrainzResource.ARTIST -> {
            ArtistsByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onArtistClick = onItemClick
            )
        }

        MusicBrainzResource.EVENT -> {
            EventsByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onEventClick = onItemClick
            )
        }

        MusicBrainzResource.INSTRUMENT -> {
            InstrumentsByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onInstrumentClick = onItemClick
            )
        }

        MusicBrainzResource.LABEL -> {
            LabelsByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onLabelClick = onItemClick
            )
        }

        MusicBrainzResource.PLACE -> {
            PlacesByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onPlaceClick = onItemClick
            )
        }

        MusicBrainzResource.RECORDING -> {
            RecordingsByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onRecordingClick = onItemClick
            )
        }

        MusicBrainzResource.RELEASE -> {
            ReleasesByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                showMoreInfo = showMoreInfoInReleaseListItem,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onReleaseClick = onItemClick
            )
        }

        MusicBrainzResource.RELEASE_GROUP -> {
            ReleaseGroupsByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                isSorted = sortReleaseGroupListItems,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onReleaseGroupClick = onItemClick,
            )
        }

        MusicBrainzResource.SERIES -> {
            SeriesByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onSeriesClick = onItemClick
            )
        }

        MusicBrainzResource.WORK -> {
            WorksByCollectionScreen(
                collectionId = collectionId,
                isRemote = isRemote,
                filterText = filterText,
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onDeleteFromCollection = onDeleteFromCollection,
                onWorkClick = onItemClick
            )
        }

        MusicBrainzResource.COLLECTION,
        MusicBrainzResource.GENRE,
        MusicBrainzResource.URL -> {
            error("Collections by ${entity.name} not supported.")
        }

        null -> {
            FullScreenLoadingIndicator()
        }
    }
}
