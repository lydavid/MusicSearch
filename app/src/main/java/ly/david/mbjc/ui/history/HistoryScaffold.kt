package ly.david.mbjc.ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryRoomModel
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScaffold(
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scope = rememberCoroutineScope()
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
            onDeleteItem = { history ->
                scope.launch {
                    viewModel.deleteHistory(mbid = history.id)

                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = "Removed ${history.title}",
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )

                    // TODO: how about marking item as deleted,
                    //  then undo just unmarks it, while dismiss actually deletes it
                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.undoDeleteHistory(history)
                        }
                        SnackbarResult.Dismissed -> {
                            // Do nothing.
                        }
                    }
                }
            }
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
