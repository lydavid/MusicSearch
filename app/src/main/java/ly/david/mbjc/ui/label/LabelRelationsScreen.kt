package ly.david.mbjc.ui.label

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.Label
import ly.david.mbjc.data.domain.RelationUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun LabelRelationsScreen(
    modifier: Modifier = Modifier,
    labelId: String,
    onTitleUpdate: (title: String) -> Unit = {},
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: LabelViewModel = hiltViewModel()
) {

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }
    var label: Label? by remember { mutableStateOf(null) }
    val lazyListState = rememberLazyListState()
//    val context = LocalContext.current

    LaunchedEffect(key1 = labelId) {
        try {
            label = viewModel.lookupLabel(labelId)
            onTitleUpdate(label?.getNameWithDisambiguation() ?: "[should not happen]")
        } catch (ex: Exception) {
            onTitleUpdate("[Label lookup failed]")
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
        prependedItems = {

        }
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
