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
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
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
import ly.david.musicsearch.core.models.ActionableResult
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.navigation.Destination
import ly.david.musicsearch.core.models.navigation.getTopLevelDestination
import ly.david.musicsearch.core.models.navigation.getTopLevelRoute
import ly.david.musicsearch.shared.feature.collections.components.CollectionBottomSheet
import ly.david.ui.commonlegacy.rememberFlowWithLifecycleStarted
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopLevelScaffold(
    navController: NavHostController,
    viewModel: TopLevelViewModel = koinViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
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
        viewModel.login(result)
    }

    suspend fun showSnackbarAndHandleResult(actionableResult: ActionableResult) {
        val snackbarResult = snackbarHostState.showSnackbar(
            message = actionableResult.message,
            actionLabel = actionableResult.actionLabel,
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
            onCreateCollectionClick = {
                // TODO: support creating collection from bottom sheet once we work on details screen mvi
            },
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

//    Scaffold(
//        bottomBar = {
//            BottomNavigationBar(
//                currentTopLevelScreen = currentTopLevelDestination,
//                navigateToTopLevelScreen = { it.onTopLevelDestinationClick() },
//            )
//        },
//        snackbarHost = {
//            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
//                SwipeToDismissBox(
//                    state = rememberSwipeToDismissBoxState(),
//                    backgroundContent = {},
//                    content = { Snackbar(snackbarData) },
//                )
//            }
//        },
//    ) { innerPadding ->
//
//        NavigationGraph(
//            navController = navController,
//            modifier = Modifier.padding(innerPadding),
//        )
//    }
}
