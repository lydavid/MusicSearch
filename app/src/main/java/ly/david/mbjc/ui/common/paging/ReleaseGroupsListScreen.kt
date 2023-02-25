package ly.david.mbjc.ui.common.paging

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.ListSeparator
import ly.david.data.domain.ReleaseGroupListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.coverart.GetReleaseGroupCoverArtPath
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.coverart.UpdateReleaseGroupCoverArtDao
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.mbjc.ui.releasegroup.ReleaseGroupListItem

@HiltViewModel
internal class ReleaseGroupsListViewModel @Inject constructor(
    override val coverArtArchiveApiService: CoverArtArchiveApiService,
    private val releaseGroupDao: ReleaseGroupDao
) : ViewModel(), GetReleaseGroupCoverArtPath {
    override val updateReleaseGroupCoverArtDao: UpdateReleaseGroupCoverArtDao
        get() = releaseGroupDao
}

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
                            // Do nothing.
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
