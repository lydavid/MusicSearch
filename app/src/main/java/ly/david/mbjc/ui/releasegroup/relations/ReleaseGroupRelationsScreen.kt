package ly.david.mbjc.ui.releasegroup.relations

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = releaseGroupId) {

        try {
//            viewModel.lookupReleaseGroupRelations(releaseGroupId)
            viewModel.resourceId.value = releaseGroupId
        } catch (ex: Exception) {
            // TODO: is error already handled below? apparently not...
            //  handle errors when relation screen is the starting tab, and when it isn't
            Log.d("Remove This", "ReleaseGroupRelationsScreen: lookup failed")
        }
        lookupInProgress = false
    }

    val lazyPagingItems: LazyPagingItems<UiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
            .collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        somethingElseLoading = lookupInProgress,
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
