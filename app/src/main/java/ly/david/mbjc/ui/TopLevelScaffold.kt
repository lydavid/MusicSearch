package ly.david.mbjc.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.navigation.Destination
import ly.david.data.navigation.getTopLevelDestination
import ly.david.data.navigation.getTopLevelRoute
import ly.david.mbjc.ui.common.dialog.CreateCollectionDialog
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.navigation.BottomNavigationBar
import ly.david.mbjc.ui.navigation.NavigationGraph
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopLevelScaffold(
    navController: NavHostController,
    viewModel: TopLevelViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val sortReleaseGroupListItems by viewModel.appPreferences.sortReleaseGroupListItems.collectAsState(initial = false)
    val showMoreInfoInReleaseListItem
        by viewModel.appPreferences.showMoreInfoInReleaseListItem.collectAsState(initial = true)

    val scope = rememberCoroutineScope()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberSheetState()
    var showCreateCollectionDialog by rememberSaveable { mutableStateOf(false) }
    var selectedEntityId by rememberSaveable { mutableStateOf("") }
    val collections: LazyPagingItems<CollectionListItemModel> = rememberFlowWithLifecycleStarted(viewModel.collections)
        .collectAsLazyPagingItems()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Destination.LOOKUP.route
    val currentTopLevelDestination: Destination = currentRoute.getTopLevelRoute().getTopLevelDestination()

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

    val loginLauncher = rememberLauncherForActivityResult(contract = viewModel.getLoginContract()) { result ->
        when {
            result.exception != null -> {
                Timber.e(result.exception)
            }
            result.response != null -> {
                viewModel.performTokenRequest(result.response)
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
            }
        )
    }

    if (openBottomSheet) {
        CollectionBottomSheet(
            bottomSheetState = bottomSheetState,
            scope = scope,
            collections = collections,
            onDismiss = { openBottomSheet = false },
            onCreateCollectionClick = { showCreateCollectionDialog = true },
            onAddToCollection = { collectionId ->
                scope.launch {
                    if (selectedEntityId.isNotEmpty()) {
                        val resultMessage = viewModel.addToCollectionAndGetResult(collectionId, selectedEntityId)
                        snackbarHostState.showSnackbar(
                            message = resultMessage,
                            withDismissAction = true
                        )
                    }
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentTopLevelDestination = currentTopLevelDestination,
                navigateToTopLevelDestination = { it.onTopLevelDestinationClick() }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->

        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onLoginClick = {
                loginLauncher.launch(Unit)
            },
            onLogoutClick = {
                viewModel.logout()
            },
            onAddToCollectionMenuClick = {
                openBottomSheet = true
            },
            onSelectedEntityChange = { entity, id ->
                viewModel.setEntity(entity)
                selectedEntityId = id
            },
            onCreateCollectionClick = {
                showCreateCollectionDialog = true
            },
            showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
            onShowMoreInfoInReleaseListItemChange = { viewModel.appPreferences.setShowMoreInfoInReleaseListItem(it) },
            sortReleaseGroupListItems = sortReleaseGroupListItems,
            onSortReleaseGroupListItemsChange = { viewModel.appPreferences.setSortReleaseGroupListItems(it) }
        )
    }
}
