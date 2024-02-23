package ly.david.musicsearch.feature.search.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import ly.david.musicsearch.core.models.network.searchableEntities
import ly.david.musicsearch.feature.search.SearchScreen
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.ExposedDropdownMenuBox
import ly.david.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Search(
    uiState: SearchScreen.UiState,
    modifier: Modifier,
//    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit,
) {
    val strings = LocalStrings.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ScrollableTopAppBar(
                showBackButton = false,
                title = strings.searchMusicbrainz,
            )
        },
    ) { innerPadding ->
        Search(
            uiState = uiState,
            modifier = Modifier.padding(innerPadding),
            snackbarHostState = snackbarHostState,
            //            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun Search(
    uiState: SearchScreen.UiState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
//    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val strings = LocalStrings.current
    val eventSink = uiState.eventSink
    val searchResultsListState = rememberLazyListState()
    val searchHistoryListState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
//                    .semantics {
//                        testTagsAsResourceId = true
//                    }
//                    .testTag(SearchScreenTestTag.TEXT_FIELD.name)
                    .weight(1f)
                    .focusRequester(focusRequester),
                shape = RectangleShape,
                value = uiState.query,
                label = { Text(strings.search) },
                placeholder = { Text(strings.search) },
                maxLines = 1,
                singleLine = true,
                trailingIcon = {
                    if (uiState.query.isBlank()) return@TextField
                    IconButton(onClick = {
                        eventSink(SearchScreen.UiEvent.UpdateQuery(""))
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
                        eventSink(SearchScreen.UiEvent.UpdateQuery(newText))
                    }
                },
            )

            ExposedDropdownMenuBox(
                modifier = Modifier
//                    .semantics {
//                        testTagsAsResourceId = true
//                    }
//                    .testTag(SearchScreenTestTag.DROPDOWN.name)
                    .weight(1f),
                options = searchableEntities,
                selectedOption = uiState.entity,
                onSelectOption = { entity ->
                    eventSink(SearchScreen.UiEvent.UpdateEntity(entity))
                },
            )
        }

        if (uiState.query.isBlank()) {
            SearchHistoryScreen(
                lazyPagingItems = uiState.searchHistory,
                lazyListState = searchHistoryListState,
                onItemClick = { entity, query ->
                    eventSink(SearchScreen.UiEvent.UpdateEntity(entity))
                    eventSink(SearchScreen.UiEvent.UpdateQuery(query))
                },
                onDeleteItem = {
                    eventSink(SearchScreen.UiEvent.DeleteSearchHistory(it))
                },
                onDeleteAllHistory = {
                    eventSink(SearchScreen.UiEvent.DeleteAllEntitySearchHistory)
                },
            )
        } else {
            SearchResultsScreen(
                lazyPagingItems = uiState.searchResults,
                lazyListState = searchResultsListState,
                snackbarHostState = snackbarHostState,
                onItemClick = { entity, id, title ->
                    eventSink(SearchScreen.UiEvent.RecordSearch)

//                    onItemClick(
//                        entity,
//                        id,
//                        title,
//                    )
                },
            )
        }
    }
}
