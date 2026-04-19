package ly.david.musicsearch.shared.feature.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.network.searchableEntities
import ly.david.musicsearch.ui.common.ResourceDropdownPicker
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Link
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiEvent
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.LookupUrlScreen
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.clearSearch
import musicsearch.ui.common.generated.resources.lookupUrl
import musicsearch.ui.common.generated.resources.search
import musicsearch.ui.common.generated.resources.searchMusicbrainz
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SearchUi(
    state: SearchUiState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = state.scrollToHideTopAppBar,
        snackbarHostState = snackbarHostState,
        topBar = { scrollBehavior ->
            ScrollableTopAppBar(
                showBackButton = false,
                title = stringResource(Res.string.searchMusicbrainz),
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.lookupUrl)) },
                        leadingIcon = {
                            Icon(
                                imageVector = CustomIcons.Link,
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            state.eventSink(SearchUiEvent.GoToScreen(LookupUrlScreen()))
                            closeMenu()
                        },
                    )
                },
            )
        },
    ) { innerPadding, scrollBehavior ->
        SearchUiContent(
            state = state,
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            eventSink = state.eventSink,
        )
    }
}

@Composable
private fun SearchUiContent(
    state: SearchUiState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    eventSink: (SearchUiEvent) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val overlayHost = LocalOverlayHost.current
    val coroutineScope = rememberCoroutineScope()

    val selectedEntityType = state.entityType
    val loginEventSink = state.musicBrainzLoginUiState.eventSink

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .testTag(SearchScreenTestTag.TEXT_FIELD.name)
                    .weight(1f)
                    .focusRequester(focusRequester),
                shape = RectangleShape,
                value = state.query,
                label = { Text(stringResource(Res.string.search)) },
                placeholder = { Text(stringResource(Res.string.search)) },
                maxLines = 1,
                singleLine = true,
                trailingIcon = {
                    if (state.query.isBlank()) return@TextField
                    IconButton(onClick = {
                        eventSink(SearchUiEvent.UpdateQuery(""))
                        focusRequester.requestFocus()
                    }) {
                        Icon(
                            CustomIcons.Clear,
                            contentDescription = stringResource(Res.string.clearSearch),
                        )
                    }
                },
                onValueChange = { newText ->
                    if (!newText.contains("\n")) {
                        eventSink(SearchUiEvent.UpdateQuery(newText))
                        coroutineScope.launch {
                            state.searchResultsListState.scrollToItem(0)
                        }
                    }
                },
            )

            ResourceDropdownPicker(
                modifier = Modifier
                    .weight(1f),
                options = searchableEntities,
                selectedOption = selectedEntityType,
                onSelectOption = { entity ->
                    eventSink(SearchUiEvent.UpdateEntity(entity))
                    coroutineScope.launch {
                        state.searchResultsListState.scrollToItem(0)
                    }
                },
            )
        }

        if (state.query.isBlank()) {
            SearchHistoryUi(
                lazyPagingItems = state.searchHistory,
                lazyListState = state.searchHistoryListState,
                onItemClick = { entityType, query ->
                    eventSink(SearchUiEvent.UpdateEntity(entityType))
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
            SearchResultsUi(
                lazyPagingItems = state.searchResults,
                lazyListState = state.searchResultsListState,
                onEditCollectionClick = {
                    showAddToCollectionSheet(
                        coroutineScope = coroutineScope,
                        overlayHost = overlayHost,
                        entityType = selectedEntityType,
                        entityIds = listOf(it),
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                        },
                    )
                },
                onItemClick = { entity, id ->
                    focusManager.clearFocus()
                    eventSink(SearchUiEvent.RecordSearch)
                    eventSink(
                        SearchUiEvent.GoToScreen(
                            DetailsScreen(
                                entityType = entity,
                                id = id,
                            ),
                        ),
                    )
                },
            )
        }
    }
}
