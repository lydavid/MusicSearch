package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.fullscreen.FullScreenText
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.TopAppBarWithFilter

/**
 * A single MusicBrainz collection.
 * Displays all items in this collection.
 *
 * User must be authenticated to view non-cached private collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionUi(
    state: CollectionUiState,
    modifier: Modifier = Modifier,
//    onBack: () -> Unit = {},
//    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
//    showMoreInfoInReleaseListItem: Boolean = true,
//    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
//    sortReleaseGroupListItems: Boolean = false,
//    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {},
//    onDeleteFromCollection: (collectableId: String, name: String) -> Unit = { _, _ -> },
//    viewModel: CollectionPresenter = koinViewModel(),
) {
    val eventSink = state.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

//    var collection: CollectionListItemModel? by remember { mutableStateOf(null) }
//    var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
//    var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
//    var filterText by rememberSaveable { mutableStateOf("") }

//    LaunchedEffect(key1 = collectionId) {
//        collection = viewModel.getCollection(collectionId)
//        entity = collection?.entity
//        isRemote = collection?.isRemote ?: false
//    }
    val collection = state.collection

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(CollectionUiEvent.NavigateUp)
                },
                title = collection?.name.orEmpty(),
                scrollBehavior = scrollBehavior,
                showFilterIcon = true,
                overflowDropdownMenuItems = {
                    if (collection?.isRemote == true) {
                        OpenInBrowserMenuItem(
                            entity = MusicBrainzEntity.COLLECTION,
                            entityId = collection.id,
                        )
                    }
                    CopyToClipboardMenuItem(collection?.id.orEmpty())
                    if (collection?.entity == MusicBrainzEntity.RELEASE_GROUP) {
//                        ToggleMenuItem(
//                            toggleOnText = strings.sort,
//                            toggleOffText = strings.unsort,
//                            toggled = sortReleaseGroupListItems,
//                            onToggle = onSortReleaseGroupListItemsChange,
//                        )
                    }
                    if (collection?.entity == MusicBrainzEntity.RELEASE) {
//                        ToggleMenuItem(
//                            toggleOnText = strings.showMoreInfo,
//                            toggleOffText = strings.showLessInfo,
//                            toggled = showMoreInfoInReleaseListItem,
//                            onToggle = onShowMoreInfoInReleaseListItemChange,
//                        )
                    }
                },
                filterText = state.query,
                onFilterTextChange = {
                    eventSink(CollectionUiEvent.UpdateQuery(it))
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
            CollectionPlatformContent(
                collectionId = collection.id,
                lazyPagingItems = state.lazyPagingItems,
                isRemote = collection.isRemote,
                filterText = state.query,
                entity = collection.entity,
                snackbarHostState = snackbarHostState,
                innerPadding = innerPadding,
                scrollBehavior = scrollBehavior,
//                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
//                sortReleaseGroupListItems = sortReleaseGroupListItems,
                onItemClick = { entity, id, title ->
                    eventSink(
                        CollectionUiEvent.ClickItem(
                            entity,
                            id,
                            title,
                        ),
                    )
                },
//                onDeleteFromCollection = onDeleteFromCollection,
            )
        }
    }
}
