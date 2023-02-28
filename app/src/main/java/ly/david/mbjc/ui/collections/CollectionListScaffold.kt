package ly.david.mbjc.ui.collections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.navigation.Destination
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.dialog.CreateCollectionDialog
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * Presents a list of all of your collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionListScaffold(
    modifier: Modifier = Modifier,
    onDestinationClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: CollectionViewModel = hiltViewModel()
) {

    var filterText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.collections)
        .collectAsLazyPagingItems()

    var showDialog by rememberSaveable { mutableStateOf(false) }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipHalfExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberSheetState(skipHalfExpanded = skipHalfExpanded)

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

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = stringResource(id = R.string.collections),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.updateQuery(query = filterText)
                },
                additionalActions = {
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.create_collection)
                        )
                    }
                }
            )
        },
    ) { innerPadding ->

        PagingLoadingAndErrorHandler(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            lazyPagingItems = lazyPagingItems,
        ) { collectionListItemModel: CollectionListItemModel? ->
            when (collectionListItemModel) {
                is CollectionListItemModel -> {
                    CollectionListItem(
                        collectionListItemModel
                    )
                }
                else -> {
                    // Do nothing.
                }
            }
        }

        // TODO: use bottom sheet for viewing collections from entity pages
        //  include button to pop up create dialog
        // Sheet content
        if (openBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false },
                sheetState = bottomSheetState,

                ) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        // Note: If you provide logic outside of onDismissRequest to remove the sheet,
                        // you must additionally handle intended state cleanup, if any.
                        onClick = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    openBottomSheet = false
                                }
                            }
                        }
                    ) {
                        Text("Hide Bottom Sheet")
                    }
                }
                LazyColumn {
                    items(50) {
                        ListItem(
                            headlineText = { Text("Item $it") },
                            leadingContent = {
                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = "Localized description"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            CollectionListScaffold()
        }
    }
}
