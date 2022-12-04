package ly.david.mbjc.ui.event

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
import ly.david.data.domain.EventListItemModel
import ly.david.data.domain.Header
import ly.david.data.domain.RelationListItemModel
import ly.david.data.domain.ListItemModel
import ly.david.data.getLifeSpanForDisplay
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.relation.RelationListItem

@Composable
internal fun EventScreen(
    modifier: Modifier = Modifier,
    eventId: String,
    onTitleUpdate: (title: String) -> Unit = { },
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: EventViewModel = hiltViewModel()
) {

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }
    var event: EventListItemModel? by remember { mutableStateOf(null) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = eventId) {
        try {
            event = viewModel.lookupEvent(eventId)
            onTitleUpdate(
                event?.getNameWithDisambiguation() ?: "[should not happen]",
            )
        } catch (ex: Exception) {
            onTitleUpdate("[Event lookup failed]")
        }
        lookupInProgress = false
    }

    val lazyPagingItems: LazyPagingItems<ListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
            .collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        somethingElseLoading = lookupInProgress,
        lazyListState = lazyListState,
    ) { listItemModel: ListItemModel? ->

        when (listItemModel) {
            is Header -> {
                Column {
                    Text(text = event?.type.orEmpty())
                    Text(text = event?.lifeSpan.getLifeSpanForDisplay())
                }
            }
            is RelationListItemModel -> {
                RelationListItem(
                    relation = listItemModel,
                    onItemClick = onItemClick,
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
