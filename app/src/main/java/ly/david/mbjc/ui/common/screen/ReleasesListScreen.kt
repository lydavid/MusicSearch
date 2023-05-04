package ly.david.mbjc.ui.common.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.listitem.SwipeToDeleteListItem
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.release.ReleaseListItem
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ReleasesListScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    showMoreInfo: Boolean = true,
    onReleaseClick: (entity: MusicBrainzResource, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
    viewModel: ReleasesListViewModel = hiltViewModel()
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { releaseListItemModel: ReleaseListItemModel? ->
        when (releaseListItemModel) {
            is ReleaseListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        ReleaseListItem(
                            release = releaseListItemModel,
                            modifier = Modifier.animateItemPlacement(),
                            showMoreInfo = showMoreInfo,
                            requestForMissingCoverArtPath = {
                                try {
                                    viewModel.getReleaseCoverArtPathFromNetwork(releaseId = releaseListItemModel.id)
                                } catch (ex: Exception) {
                                    Timber.e(ex)
                                }
                            }
                        ) {
                            onReleaseClick(MusicBrainzResource.RELEASE, id, getNameWithDisambiguation())
                        }
                    },
                    disable = onDeleteFromCollection == null,
                    onDelete = {
                        onDeleteFromCollection?.invoke(releaseListItemModel.id, releaseListItemModel.name)
                    }
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
