package ly.david.mbjc.ui.collections

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.data.domain.CollectionListItemModel
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.dialog.CreateCollectionDialog
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * Displays a list of all of your collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionListScaffold(
    modifier: Modifier = Modifier,
    onCollectionClick: (id: String) -> Unit = {},
    viewModel: CollectionViewModel = hiltViewModel()
) {

    var filterText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.collections)
        .collectAsLazyPagingItems()

    var showDialog by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // TODO: we might be able to hoist this up to NavigationGraph, then we don't need to repeat it everywhere
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
                        collection = collectionListItemModel,
                        onClick = { onCollectionClick(id) }
                    )
                }
                else -> {
                    // Do nothing.
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
