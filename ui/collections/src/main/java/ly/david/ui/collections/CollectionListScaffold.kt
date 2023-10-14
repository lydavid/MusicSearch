package ly.david.ui.collections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.data.core.listitem.CollectionListItemModel
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import org.koin.androidx.compose.koinViewModel

/**
 * Displays a list of all of your collections.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CollectionListScaffold(
    modifier: Modifier = Modifier,
    onCollectionClick: (id: String, isRemote: Boolean) -> Unit = { _, _ -> },
    onCreateCollectionClick: () -> Unit = {},
    viewModel: CollectionListViewModel = koinViewModel(),
) {
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var filterText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.pagedEntities)
        .collectAsLazyPagingItems()
    val showLocal by viewModel.appPreferences.showLocalCollections.collectAsState(initial = true)
    val showRemote by viewModel.appPreferences.showRemoteCollections.collectAsState(initial = true)

    LaunchedEffect(Unit) {
        viewModel.setShowLocal(showLocal)
        viewModel.setShowRemote(showRemote)
        viewModel.getUsernameThenLoadCollections()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = strings.collections,
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
                            contentDescription = strings.createCollection,
                        )
                    }
                },
                additionalBar = {
                    CollectionsFilterChipsBar(
                        showLocal = showLocal,
                        onShowLocalToggle = viewModel::setShowLocal,
                        showRemote = showRemote,
                        onShowRemoteToggle = viewModel::setShowRemote,
                    )
                },
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
                        onClick = { onCollectionClick(id, isRemote) },
                    )
                }

                else -> {
                    // Do nothing.
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectionsFilterChipsBar(
    modifier: Modifier = Modifier,
    showLocal: Boolean = true,
    onShowLocalToggle: (Boolean) -> Unit = {},
    showRemote: Boolean = true,
    onShowRemoteToggle: (Boolean) -> Unit = {},
) {
    Row(modifier = modifier.padding(horizontal = 16.dp)) {
        FilterChip(
            selected = showLocal,
            onClick = { onShowLocalToggle(!showLocal) },
            label = { Text(text = "Local") },
            leadingIcon = {
                if (showLocal) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                }
            },
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        FilterChip(
            selected = showRemote,
            onClick = { onShowRemoteToggle(!showRemote) },
            label = { Text(text = "Remote") },
            leadingIcon = {
                if (showRemote) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                }
            },
        )
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        var showLocal by rememberSaveable { mutableStateOf(true) }
        var showRemote by rememberSaveable { mutableStateOf(true) }

        Surface {
            CollectionsFilterChipsBar(
                showLocal = showLocal,
                onShowLocalToggle = { showLocal = it },
                showRemote = showRemote,
                onShowRemoteToggle = { showRemote = it },
            )
        }
    }
}
