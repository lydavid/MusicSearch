package ly.david.mbjc.ui.common.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.ListSeparator
import ly.david.data.domain.ReleaseGroupListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.releasegroup.ReleaseGroupListItem
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleaseGroupsListScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    onReleaseGroupClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
    viewModel: ReleaseGroupsListViewModel = hiltViewModel()
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ReleaseGroupListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        ReleaseGroupListItem(
                            releaseGroup = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                            requestForMissingCoverArtPath = {
                                try {
                                    viewModel.getReleaseGroupCoverArtPathFromNetwork(releaseGroupId = listItemModel.id)
                                } catch (ex: Exception) {
                                    Timber.e(ex)
                                }
                            }
                        ) {
                            onReleaseGroupClick(MusicBrainzResource.RELEASE_GROUP, id, getNameWithDisambiguation())
                        }
                    },
                    disable = onDeleteFromCollection == null,
                    onDelete = {
                        onDeleteFromCollection?.invoke(listItemModel.id, listItemModel.name)
                    }
                )
            }

            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
