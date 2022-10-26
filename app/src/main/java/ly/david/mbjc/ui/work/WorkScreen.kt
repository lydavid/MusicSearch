package ly.david.mbjc.ui.work

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.UiModel
import ly.david.data.domain.WorkUiModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun WorkScreen(
    modifier: Modifier = Modifier,
    workId: String,
    onTitleUpdate: (title: String, subtitle: String) -> Unit = { _, _ -> },
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: WorkViewModel = hiltViewModel()
) {

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }
    var work: WorkUiModel? by remember { mutableStateOf(null) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = workId) {
        try {
            work = viewModel.lookupWork(workId)
            onTitleUpdate(
                work?.getNameWithDisambiguation() ?: "[should not happen]",
                "[Work by <artist name>]"
            )
        } catch (ex: Exception) {
            onTitleUpdate("[Work lookup failed]", "[error]")
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
            is ly.david.data.domain.Header -> {
                Column {
                    Text(text = work?.type.orEmpty())
                    Text(text = work?.language.orEmpty())
                }
            }
            is ly.david.data.domain.RelationUiModel -> {
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
