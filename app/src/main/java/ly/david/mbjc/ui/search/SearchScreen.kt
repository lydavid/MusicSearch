package ly.david.mbjc.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.core.network.searchableEntities
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.ui.common.ExposedDropdownMenuBox
import ly.david.ui.common.R
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    initialQuery: String? = null,
    initialEntity: MusicBrainzEntity? = null,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val searchResults: LazyPagingItems<ListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.searchResults)
            .collectAsLazyPagingItems()
    val searchResultsListState = rememberLazyListState()

    val searchHistory: LazyPagingItems<ListItemModel> =
        rememberFlowWithLifecycleStarted(viewModel.searchHistory)
            .collectAsLazyPagingItems()
    val searchHistoryListState = rememberLazyListState()

    val queryText by viewModel.searchQuery.collectAsState()
    val selectedEntity by viewModel.searchEntity.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    fun search(query: String? = null, entity: MusicBrainzEntity? = null) {
        coroutineScope.launch {
            viewModel.search(query = query, entity = entity)
            searchResultsListState.scrollToItem(0)
        }
    }

    LaunchedEffect(key1 = initialQuery, key2 = initialEntity) {
        if (initialQuery == null || initialEntity == null) return@LaunchedEffect
        viewModel.search(initialQuery, initialEntity)
    }

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .testTag(SearchScreenTestTag.TEXT_FIELD.name)
                    .weight(1f)
                    .focusRequester(focusRequester),
                shape = RectangleShape,
                value = queryText,
                label = { Text(stringResource(id = R.string.search)) },
                placeholder = { Text(stringResource(id = R.string.search)) },
                maxLines = 1,
                singleLine = true,
                trailingIcon = {
                    if (queryText.isEmpty()) return@TextField
                    IconButton(onClick = {
                        viewModel.clearQuery()
                        focusRequester.requestFocus()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = stringResource(id = R.string.clear_search))
                    }
                },
                onValueChange = { newText ->
                    if (!newText.contains("\n")) {
                        search(query = newText)
                    }
                }
            )

            ExposedDropdownMenuBox(
                modifier = Modifier.weight(1f),
                options = searchableEntities,
                selectedOption = selectedEntity,
                onSelectOption = { entity ->
                    search(entity = entity)
                }
            )
        }

        if (queryText.isEmpty()) {
            SearchHistoryScreen(
                lazyPagingItems = searchHistory,
                lazyListState = searchHistoryListState,
                onItemClick = { entity, query ->
                    search(query = query, entity = entity)
                },
                onDeleteItem = viewModel::deleteSearchHistoryItem,
                onDeleteAllHistory = viewModel::deleteAllSearchHistoryForEntity
            )
        } else {
            SearchResultsScreen(
                lazyPagingItems = searchResults,
                lazyListState = searchResultsListState,
                snackbarHostState = snackbarHostState,
                onItemClick = { entity, id, title ->
                    viewModel.recordSearchHistory()
                    onItemClick(entity, id, title)
                }
            )
        }
    }
}

enum class SearchScreenTestTag {
    TEXT_FIELD,
}
