package ly.david.musicsearch.shared.feature.search.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import ly.david.musicsearch.core.models.network.searchableEntities
import ly.david.musicsearch.shared.feature.search.SearchScreenTestTag
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.ui.common.ExposedDropdownMenuBox
import ly.david.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Search(
    state: SearchUiState,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ScrollableTopAppBar(
                showBackButton = false,
                title = strings.searchMusicbrainz,
            )
        },
    ) { innerPadding ->
        Search(
            state = state,
            modifier = Modifier.padding(innerPadding),
            snackbarHostState = snackbarHostState,
        )
    }
}

@Composable
private fun Search(
    state: SearchUiState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
) {
    val strings = LocalStrings.current
    val eventSink = state.eventSink
    val searchResultsListState = rememberLazyListState()
    val searchHistoryListState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .testTag(SearchScreenTestTag.TEXT_FIELD.name)
                    .weight(1f)
                    .focusRequester(focusRequester),
                shape = RectangleShape,
                value = state.query,
                label = { Text(strings.search) },
                placeholder = { Text(strings.search) },
                maxLines = 1,
                singleLine = true,
                trailingIcon = {
                    if (state.query.isBlank()) return@TextField
                    IconButton(onClick = {
                        eventSink(SearchUiEvent.UpdateQuery(""))
                        focusRequester.requestFocus()
                    }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = strings.clearSearch,
                        )
                    }
                },
                onValueChange = { newText ->
                    if (!newText.contains("\n")) {
                        eventSink(SearchUiEvent.UpdateQuery(newText))
                    }
                },
            )

            ExposedDropdownMenuBox(
                modifier = Modifier
                    .weight(1f),
                options = searchableEntities,
                selectedOption = state.entity,
                onSelectOption = { entity ->
                    eventSink(SearchUiEvent.UpdateEntity(entity))
                },
            )
        }

        if (state.query.isBlank()) {
            SearchHistoryScreen(
                lazyPagingItems = state.searchHistory,
                lazyListState = searchHistoryListState,
                onItemClick = { entity, query ->
                    eventSink(SearchUiEvent.UpdateEntity(entity))
                    eventSink(SearchUiEvent.UpdateQuery(query))
                },
                onDeleteItem = {
                    eventSink(SearchUiEvent.DeleteSearchHistory(it))
                },
                onDeleteAllHistory = {
                    eventSink(SearchUiEvent.DeleteAllEntitySearchHistory)
                },
            )
        } else {
            SearchResultsScreen(
                lazyPagingItems = state.searchResults,
                lazyListState = searchResultsListState,
                snackbarHostState = snackbarHostState,
                onItemClick = { entity, id, title ->
                    focusManager.clearFocus()
                    eventSink(SearchUiEvent.RecordSearch)
                    eventSink(
                        SearchUiEvent.ClickItem(
                            entity = entity,
                            id = id,
                            title = title,
                        ),
                    )
                },
            )
        }
    }
}
