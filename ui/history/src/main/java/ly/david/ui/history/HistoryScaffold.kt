package ly.david.ui.history

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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.ui.common.dialog.SimpleAlertDialog
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScaffold(
    deleteHistoryDelegate: DeleteHistoryDelegate,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: HistoryViewModel = koinViewModel(),
) {
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    var filterText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.lookUpHistory)
        .collectAsLazyPagingItems()
    val sortOption by viewModel.sortOption.collectAsState()
    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    if (showDeleteConfirmationDialog) {
        SimpleAlertDialog(
            title = strings.deleteLookupHistoryConfirmation,
            confirmText = strings.yes,
            dismissText = strings.no,
            onDismiss = { showDeleteConfirmationDialog = false },
            onConfirmClick = { deleteHistoryDelegate.deleteAll() },
        )
    }

    if (showBottomSheet) {
        HistorySortBottomSheet(
            sortOption = sortOption,
            onSortOptionClick = viewModel::updateSortOption,
            bottomSheetState = bottomSheetState,
            onDismiss = { showBottomSheet = false },
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = strings.recentHistory,
                scrollBehavior = scrollBehavior,
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.updateQuery(query = filterText)
                },
                overflowDropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(strings.clearHistory) },
                        onClick = {
                            showDeleteConfirmationDialog = true
                            closeMenu()
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(strings.sort) },
                        onClick = {
                            showBottomSheet = true
                            closeMenu()
                        },
                    )
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SwipeToDismiss(
                    state = rememberDismissState(),
                    background = {},
                    dismissContent = { Snackbar(snackbarData) },
                )
            }
        },
    ) { innerPadding ->
        HistoryScreen(
            lazyPagingItems = lazyPagingItems,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            onItemClick = onItemClick,
            onDeleteItem = deleteHistoryDelegate::delete,
        )
    }
}
