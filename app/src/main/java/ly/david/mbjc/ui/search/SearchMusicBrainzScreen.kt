package ly.david.mbjc.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.mbjc.R
import ly.david.mbjc.data.domain.EndOfList
import ly.david.mbjc.data.domain.UiArtist
import ly.david.mbjc.data.domain.UiData
import ly.david.mbjc.data.domain.UiReleaseGroup
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.Destination
import ly.david.mbjc.ui.artist.ArtistCard
import ly.david.mbjc.ui.common.SimpleAlertDialog
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.releasegroup.ReleaseGroupCard

@Composable
fun SearchMusicBrainzScreen(
    lazyListState: LazyListState = rememberLazyListState(),
    snackbarHostState: SnackbarHostState,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: SearchMusicBrainzViewModel = hiltViewModel()
) {

    val lazyPagingItems: LazyPagingItems<UiData> = viewModel.searchResultsUiData.collectAsLazyPagingItems()

    var text by rememberSaveable { mutableStateOf("") }
    var selectedOption by rememberSaveable { mutableStateOf(MusicBrainzResource.ARTIST) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }

    if (showAlertDialog) {
        SimpleAlertDialog(
            title = "Search cannot be empty",
            confirmText = "OK",
            onDismiss = { showAlertDialog = false }
        )
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                value = text,
                label = { Text("Search") },
                placeholder = { Text("Search") },
                maxLines = 1, // TODO: Seems like this is currently broken
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        coroutineScope.launch {
                            if (text.isEmpty()) {
                                showAlertDialog = true
                            } else {
                                viewModel.updateViewModelState(selectedOption, text)
                                lazyListState.scrollToItem(0)
                                focusManager.clearFocus()
                            }
                        }
                    }
                ),
                trailingIcon = {
                    if (text.isEmpty()) return@TextField
                    IconButton(onClick = {
                        text = ""
                        focusRequester.requestFocus()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search field.")
                    }
                },
                onValueChange = { newText ->
                    text = newText
                }
            )

            // TODO: this doesn't fill rest of screen despite weight 1f
            // TODO: focusing on this requires 1-2 additional backpresses to exit app
            ExposedDropdownMenuBox(
                modifier = Modifier.weight(1f),
                options = MusicBrainzResource.values().toList(),
                selectedOption = selectedOption,
                onSelectOption = {
                    selectedOption = it
                }
            )
        }

        PagingLoadingAndErrorHandler(
            lazyPagingItems = lazyPagingItems,
            lazyListState = lazyListState,
            snackbarHostState = snackbarHostState,
            noResultsText = stringResource(id = R.string.no_results_found_search)
        ) { uiData: UiData? ->
            when (uiData) {
                is UiArtist -> {
                    ArtistCard(artist = uiData) {
                        onItemClick(Destination.LOOKUP_ARTIST, it.id)
                    }
                }
                is UiReleaseGroup -> {
                    // TODO: should see album type rather than year
                    ReleaseGroupCard(releaseGroup = uiData) {
                        onItemClick(Destination.LOOKUP_RELEASE_GROUP, it.id)
                    }
                }
                is EndOfList -> {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        text = "End of search results."
                    )
                }
                else -> {
                    // Null. Do nothing.
                }
            }

        }
    }
}
