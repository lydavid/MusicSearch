package ly.david.mbjc.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.mbjc.ui.navigation.BottomNavigationBar
import ly.david.mbjc.ui.navigation.NavigationGraph
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.core.models.navigation.Destination
import ly.david.musicsearch.core.models.navigation.getTopLevelDestination
import ly.david.musicsearch.core.models.navigation.getTopLevelRoute
import ly.david.ui.collections.CollectionBottomSheet
import ly.david.ui.collections.CreateCollectionDialog
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.history.DeleteHistoryDelegate
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopLevelScaffold(
    navController: NavHostController,
    viewModel: TopLevelViewModel = koinViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val sortReleaseGroupListItems
        by viewModel.appPreferences.sortReleaseGroupListItems.collectAsState(initial = false)
    val showMoreInfoInReleaseListItem
        by viewModel.appPreferences.showMoreInfoInReleaseListItem.collectAsState(initial = true)

    val scope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    var showCreateCollectionDialog by rememberSaveable { mutableStateOf(false) }
    val collections: LazyPagingItems<CollectionListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.collections)
            .collectAsLazyPagingItems()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Destination.LOOKUP.route
    val currentTopLevelDestination: Destination =
        currentRoute.getTopLevelRoute().getTopLevelDestination()

    val onTopLevelDestinationClick: Destination.() -> Unit = {
        navController.navigate(name) {
            // Top-level screens should use this to prevent selecting the same screen.
            launchSingleTop = true

            // Selecting a top-level screen should remove all backstack.
            popUpTo(navController.graph.findStartDestination().id) {
                // And it should not save the state of the previous screen.
                saveState = false
            }
        }
    }

    // TODO: extract so that we can login without ref to AppAuth's AuthorizationResponse
    val loginLauncher = rememberLauncherForActivityResult(contract = viewModel.getLoginContract()) {
        val result = it
        val exception = result.exception
        val response = result.response
        when {
            exception != null -> {
                Timber.e(exception)
            }

            response != null -> {
                viewModel.login(response)
            }

            else -> {
                Timber.e("login's result intent is null")
            }
        }
    }

    if (showCreateCollectionDialog) {
        CreateCollectionDialog(
            onDismiss = { showCreateCollectionDialog = false },
            onSubmit = { name, entity ->
                viewModel.createNewCollection(name, entity)
            },
        )
    }

    suspend fun showSnackbarAndHandleResult(remoteResult: TopLevelViewModel.RemoteResult) {
        val snackbarResult = snackbarHostState.showSnackbar(
            message = remoteResult.message,
            actionLabel = remoteResult.actionLabel,
            duration = SnackbarDuration.Short,
        )

        when (snackbarResult) {
            SnackbarResult.ActionPerformed -> {
                loginLauncher.launch(Unit)
            }

            SnackbarResult.Dismissed -> {
                // Do nothing.
            }
        }
    }

    if (showBottomSheet) {
        CollectionBottomSheet(
            bottomSheetState = bottomSheetState,
            scope = scope,
            collections = collections,
            onDismiss = { showBottomSheet = false },
            onCreateCollectionClick = { showCreateCollectionDialog = true },
            onAddToCollection = { collectionId ->
                scope.launch {
                    val addToCollectionResult = viewModel.addToCollectionAndGetResult(
                        collectionId = collectionId,
                    )

                    if (addToCollectionResult.message.isEmpty()) return@launch

                    showSnackbarAndHandleResult(addToCollectionResult)
                }
            },
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentTopLevelDestination = currentTopLevelDestination,
                navigateToTopLevelDestination = { it.onTopLevelDestinationClick() },
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

        NavigationGraph(
            navController = navController,
            deleteHistoryDelegate = object : DeleteHistoryDelegate {
                override fun delete(history: LookupHistoryListItemModel) {
                    scope.launch {
                        viewModel.markHistoryAsDeleted(mbid = history.id)

                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = "Removed ${history.title}",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short,
                        )

                        when (snackbarResult) {
                            SnackbarResult.ActionPerformed -> {
                                viewModel.undoDeleteHistory(history.id)
                            }

                            SnackbarResult.Dismissed -> {
                                viewModel.deleteHistory(history.id)
                            }
                        }
                    }
                }

                override fun deleteAll() {
                    scope.launch {
                        viewModel.markAllHistoryAsDeleted()

                        val snackbarResult = snackbarHostState.showSnackbar(
                            message = "Cleared history",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short,
                        )

                        when (snackbarResult) {
                            SnackbarResult.ActionPerformed -> {
                                viewModel.undoDeleteAllHistory()
                            }

                            SnackbarResult.Dismissed -> {
                                viewModel.deleteAllHistory()
                            }
                        }
                    }
                }
            },
            modifier = Modifier.padding(innerPadding),
            onLoginClick = {
                loginLauncher.launch(Unit)
            },
            onLogoutClick = {
                viewModel.logout()
            },
            onCreateCollectionClick = {
                showCreateCollectionDialog = true
            },
            onAddToCollectionMenuClick = { entity, id ->
                viewModel.setEntity(entity)
                viewModel.setEntityId(id)
                showBottomSheet = true
            },
            onDeleteFromCollection = { collectionId, entityId, name ->
                scope.launch {
                    val deleteFromCollectionResult =
                        viewModel.deleteFromCollectionAndGetResult(collectionId, entityId, name)

                    if (deleteFromCollectionResult.message.isEmpty()) return@launch

                    showSnackbarAndHandleResult(deleteFromCollectionResult)
                }
            },
            showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
            onShowMoreInfoInReleaseListItemChange = viewModel.appPreferences::setShowMoreInfoInReleaseListItem,
            sortReleaseGroupListItems = sortReleaseGroupListItems,
            onSortReleaseGroupListItemsChange = viewModel.appPreferences::setSortReleaseGroupListItems,
        )
    }
}
