package ly.david.ui.collections

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
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.ui.collections.areas.AreasByCollectionScreen
import ly.david.ui.collections.artists.ArtistsByCollectionScreen
import ly.david.ui.collections.events.EventsByCollectionScreen
import ly.david.ui.collections.instruments.InstrumentsByCollectionScreen
import ly.david.ui.collections.labels.LabelsByCollectionScreen
import ly.david.ui.collections.places.PlacesByCollectionScreen
import ly.david.ui.collections.recordings.RecordingsByCollectionScreen
import ly.david.ui.collections.releasegroups.ReleaseGroupsByCollectionScreen
import ly.david.ui.collections.releases.ReleasesByCollectionScreen
import ly.david.ui.collections.series.SeriesByCollectionScreen
import ly.david.ui.collections.works.WorksByCollectionScreen
import ly.david.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.ui.common.fullscreen.FullScreenText
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.ToggleMenuItem
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import org.koin.androidx.compose.koinViewModel

/**
 * A single MusicBrainz collection.
 * Displays all items in this collection.
 *
 * User must be authenticated to view non-cached private collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScaffold(
    collectionId: String,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    sortReleaseGroupListItems: Boolean = false,
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {},
    onDeleteFromCollection: (collectableId: String, name: String) -> Unit = { _, _ -> },
    viewModel: CollectionViewModel = koinViewModel(),
) {
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    var collection: CollectionListItemModel? by remember { mutableStateOf(null) }
    var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
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
                        OpenInBrowserMenuItem(entity = MusicBrainzEntity.COLLECTION, entityId = collectionId)
                    }
                    CopyToClipboardMenuItem(collectionId)
                    if (entity == MusicBrainzEntity.RELEASE_GROUP) {
                        ToggleMenuItem(
                            toggleOnText = strings.sort,
                            toggleOffText = strings.unsort,
                            toggled = sortReleaseGroupListItems,
                            onToggle = onSortReleaseGroupListItemsChange,
                        )
                    }
                    if (entity == MusicBrainzEntity.RELEASE) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = showMoreInfoInReleaseListItem,
                            onToggle = onShowMoreInfoInReleaseListItemChange,
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
                    .padding(innerPadding),
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
                onDeleteFromCollection = onDeleteFromCollection,
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
    entity: MusicBrainzEntity?,
    snackbarHostState: SnackbarHostState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    showMoreInfoInReleaseListItem: Boolean = true,
    sortReleaseGroupListItems: Boolean = false,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (collectableId: String, name: String) -> Unit = { _, _ -> },
) {
    when (entity) {
        MusicBrainzEntity.AREA -> {
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
                onAreaClick = onItemClick,
            )
        }

        MusicBrainzEntity.ARTIST -> {
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
                onArtistClick = onItemClick,
            )
        }

        MusicBrainzEntity.EVENT -> {
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
                onEventClick = onItemClick,
            )
        }

        MusicBrainzEntity.INSTRUMENT -> {
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
                onInstrumentClick = onItemClick,
            )
        }

        MusicBrainzEntity.LABEL -> {
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
                onLabelClick = onItemClick,
            )
        }

        MusicBrainzEntity.PLACE -> {
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
                onPlaceClick = onItemClick,
            )
        }

        MusicBrainzEntity.RECORDING -> {
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
                onRecordingClick = onItemClick,
            )
        }

        MusicBrainzEntity.RELEASE -> {
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
                onReleaseClick = onItemClick,
            )
        }

        MusicBrainzEntity.RELEASE_GROUP -> {
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

        MusicBrainzEntity.SERIES -> {
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
                onSeriesClick = onItemClick,
            )
        }

        MusicBrainzEntity.WORK -> {
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
                onWorkClick = onItemClick,
            )
        }

        MusicBrainzEntity.COLLECTION,
        MusicBrainzEntity.GENRE,
        MusicBrainzEntity.URL,
        -> {
            error("Collections by ${entity.name} not supported.")
        }

        null -> {
            FullScreenLoadingIndicator()
        }
    }
}
