package ly.david.mbjc.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopLevelScaffold(
    navController: NavHostController,
    viewModel: TopLevelViewModel = hiltViewModel()
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Note that destination?.route includes parameters such as artistId
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

    val scope = rememberCoroutineScope()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberSheetState()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var selectedEntityId by rememberSaveable { mutableStateOf("") }
    val collections: LazyPagingItems<CollectionListItemModel> = rememberFlowWithLifecycleStarted(viewModel.collections)
        .collectAsLazyPagingItems()

    if (showDialog) {
        CreateCollectionDialog(
            onDismiss = { showDialog = false },
            onSubmit = { name, entity ->
                scope.launch {
                    viewModel.createNewCollection(name, entity)
                }
            }
        )
    }

    if (openBottomSheet) {
        CollectionBottomSheet(
            bottomSheetState = bottomSheetState,
            scope = scope,
            collections = collections,
            onDismiss = { openBottomSheet = false },
            onCreateCollectionButtonClick = { showDialog = true },
            onAddToCollection = { collectionId ->
                if (selectedEntityId.isNotEmpty()) {
                    viewModel.addToCollection(collectionId, selectedEntityId)
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
        }
    ) { innerPadding ->

        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onAddToCollectionMenuClick = {
                openBottomSheet = true
            },
            onSelectedEntityChange = { entity, id ->
                viewModel.resource.value = entity
                selectedEntityId = id
            }
        )
    }
}
