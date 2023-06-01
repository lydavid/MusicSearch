package ly.david.mbjc.ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryRoomModel
import ly.david.mbjc.ui.DeleteHistoryDelegate
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScaffold(
    deleteHistoryDelegate: DeleteHistoryDelegate,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    var filterText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.lookUpHistory)
        .collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = stringResource(id = R.string.recent_history),
                scrollBehavior = scrollBehavior,
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.updateQuery(query = filterText)
                },
                overflowDropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text("Clear history") },
                        onClick = {
                            deleteHistoryDelegate.deleteAll()
                            closeMenu()
                        }
                    )
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SwipeToDismiss(
                    state = rememberDismissState(),
                    background = {},
                    dismissContent = { Snackbar(snackbarData) }
                )
            }
        },
    ) { innerPadding ->
        HistoryScreen(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            lazyPagingItems = lazyPagingItems,
            onItemClick = onItemClick,
            onDeleteItem = deleteHistoryDelegate::delete
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HistoryScreen(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<LookupHistoryRoomModel>,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteItem: (LookupHistoryRoomModel) -> Unit = {}
) {

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
    ) { lookupHistory: LookupHistoryRoomModel? ->
        when (lookupHistory) {
            is LookupHistoryRoomModel -> {
                HistoryListItem(
                    lookupHistory = lookupHistory,
                    modifier = Modifier.animateItemPlacement(),
                    onItemClick = onItemClick,
                    onDeleteItem = onDeleteItem
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
