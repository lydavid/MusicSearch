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
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.releasegroup.ReleaseGroupListItem
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ReleaseGroupsListScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    onReleaseGroupClick: (String, String) -> Unit,
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
                    onReleaseGroupClick(id, getNameWithDisambiguation())
                }
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
