package ly.david.mbjc.ui.collections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.CollectionListItemModel
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.collections.CollectionListItem
import ly.david.ui.common.R
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme

/**
 * Displays a list of all of your collections.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun CollectionListScaffold(
    modifier: Modifier = Modifier,
    onCollectionClick: (id: String, isRemote: Boolean) -> Unit = { _, _ -> },
    onCreateCollectionClick: () -> Unit = {},
    viewModel: CollectionListViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var filterText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.pagedResources)
        .collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.getUsernameThenLoadCollections()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = stringResource(id = R.string.collections),
                scrollBehavior = scrollBehavior,
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.updateQuery(query = filterText)
                },
                additionalActions = {
                    IconButton(onClick = onCreateCollectionClick) {
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
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            lazyPagingItems = lazyPagingItems,
        ) { collectionListItemModel: CollectionListItemModel? ->
            when (collectionListItemModel) {
                is CollectionListItemModel -> {
                    CollectionListItem(
                        collection = collectionListItemModel,
                        modifier = Modifier.animateItemPlacement(),
                        onClick = { onCollectionClick(id, isRemote) }
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
