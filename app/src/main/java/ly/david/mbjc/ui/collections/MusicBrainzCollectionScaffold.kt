package ly.david.mbjc.ui.collections

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.collections.releasegroups.ReleaseGroupsByCollectionScreen
import ly.david.mbjc.ui.collections.releases.ReleasesByCollectionScreen
import ly.david.mbjc.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted

/**
 * A single MusicBrainz collection.
 * Displays all items in this collection.
 *
 * User must be authenticated to view non-cached private collections.
 */
@Composable
internal fun MusicBrainzCollectionScaffold(
    collectionId: String,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: MusicBrainzCollectionViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }

    var entity: MusicBrainzResource? by rememberSaveable { mutableStateOf(null) }

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
        entity = viewModel.getCollection(collectionId).entity
    }

    when (entity) {
        MusicBrainzResource.RELEASE -> {
            ReleasesByCollectionScreen(
                collectionId = collectionId,
                filterText = "",
                releasesLazyPagingItems = releasesLazyPagingItems,
                modifier = modifier
//            .padding(innerPadding)
                    .fillMaxSize(),
//            .nestedScroll(scrollBehavior.nestedScrollConnection),
                snackbarHostState = snackbarHostState,
                releasesLazyListState = releasesLazyListState,
                onPagedReleasesFlowChange = { pagedReleasesFlow = it },
                showMoreInfo = true,
                onReleaseClick = onItemClick
            )
        }
        MusicBrainzResource.RELEASE_GROUP -> {
            ReleaseGroupsByCollectionScreen(
                collectionId = collectionId,
                modifier = Modifier
//                    .padding(innerPadding)
                    .fillMaxSize(),
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                searchText = filterText,
//                isSorted = sortReleaseGroupListItems,
                snackbarHostState = snackbarHostState,
                onReleaseGroupClick = onItemClick,
                lazyListState = releaseGroupsLazyListState,
                lazyPagingItems = releaseGroupsLazyPagingItems,
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
