package ly.david.mbjc.ui.history

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.navigation.Destination
import ly.david.data.persistence.history.LookupHistoryRoomModel
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScaffold(
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    openDrawer: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {

    var filterText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.lookUpHistory)
        .collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBarWithFilter(
                openDrawer = openDrawer,
                title = stringResource(id = R.string.recent_history),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.updateQuery(query = filterText)
                },
            )
        },
    ) {
        HistoryScreen(
            lazyPagingItems = lazyPagingItems,
            onItemClick = onItemClick
        )
    }
}

@Composable
internal fun HistoryScreen(
    lazyPagingItems: LazyPagingItems<LookupHistoryRoomModel>,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {

    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
    ) { lookupHistory: LookupHistoryRoomModel? ->
        when (lookupHistory) {
            is LookupHistoryRoomModel -> {
                HistoryListItem(
                    lookupHistory = lookupHistory,
                    onItemClick = onItemClick
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
