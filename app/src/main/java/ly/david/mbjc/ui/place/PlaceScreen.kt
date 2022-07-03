package ly.david.mbjc.ui.place

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.Place
import ly.david.mbjc.data.domain.Header
import ly.david.mbjc.data.domain.RelationUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.relation.RelationCard

@Composable
internal fun PlaceScreen(
    modifier: Modifier = Modifier,
    placeId: String,
    onTitleUpdate: (title: String) -> Unit = {},
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: PlaceViewModel = hiltViewModel()
) {

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }
    var place: Place? by remember { mutableStateOf(null) }
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(key1 = placeId) {
        try {
            place = viewModel.lookupPlace(placeId)
            onTitleUpdate(place?.getNameWithDisambiguation() ?: "[should not happen]")
        } catch (ex: Exception) {
            onTitleUpdate("[Place lookup failed]")
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
            is Header -> {
                place?.coordinates?.let {
                    CoordinateCard(
                        context = context,
                        coordinates = it,
                        label = place?.name
                    )
                }
            }
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
