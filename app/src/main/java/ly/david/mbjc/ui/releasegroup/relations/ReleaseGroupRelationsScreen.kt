package ly.david.mbjc.ui.releasegroup.relations

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.domain.RelationUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun ReleaseGroupRelationsScreen(
    modifier: Modifier = Modifier,
    releaseGroupId: String,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    lazyListState: LazyListState,
    viewModel: ReleaseGroupRelationsViewModel = hiltViewModel()
) {

//    var lookupInProgress by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = releaseGroupId) {
        viewModel.resourceId.value = releaseGroupId
//        lookupInProgress = false
    }

    // TODO: might be able to hoist this by proving a (LazyPagingItems<UiModel>) -> Unit
    val lazyPagingItems: LazyPagingItems<UiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
            .collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
//        somethingElseLoading = lookupInProgress,
        lazyListState = lazyListState,
    ) { uiModel: UiModel? ->

        when (uiModel) {
            is RelationUiModel -> {
                RelationCard(
                    relation = uiModel,
                    onItemClick = onItemClick,
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
