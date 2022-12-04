package ly.david.mbjc.ui.place

import android.content.Context
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
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.ListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.relation.RelationListItem

@Composable
internal fun PlaceScreen(
    modifier: Modifier = Modifier,
    context: Context,
    placeId: String,
    onTitleUpdate: (title: String) -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: PlaceViewModel = hiltViewModel()
) {

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }
    var place: PlaceListItemModel? by remember { mutableStateOf(null) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = placeId) {
        try {
            place = viewModel.lookupPlace(placeId)
            onTitleUpdate(place?.getNameWithDisambiguation() ?: "[should not happen]")
        } catch (ex: Exception) {
            onTitleUpdate("[Place lookup failed]")
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
            is ly.david.data.domain.Header -> {
                place?.coordinates?.let {

                    val label = place?.name +
                        if (place?.lifeSpan?.ended == true) " (closed)" else ""

                    CoordinateListItem(
                        context = context,
                        coordinates = it,
                        label = label
                    )
                }
            }
            is ly.david.data.domain.RelationListItemModel -> {
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
