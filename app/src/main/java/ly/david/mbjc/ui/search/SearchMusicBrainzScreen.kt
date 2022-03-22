package ly.david.mbjc.ui.search

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.launch
import ly.david.mbjc.R
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.domain.EndOfList
import ly.david.mbjc.data.domain.UiArtist
import ly.david.mbjc.data.domain.UiData
import ly.david.mbjc.data.domain.UiReleaseGroup
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.Destination
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.releasegroup.ReleaseGroupCard
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.mbjc.ui.theme.getAlertBackgroundColor
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
fun SearchMusicBrainzScreen(
    lazyListState: LazyListState = rememberLazyListState(),
    scaffoldState: ScaffoldState,
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

    // TODO: extract & generalize
    if (showAlertDialog) {
        AlertDialog(
            title = { Text("Search cannot be empty") },
            backgroundColor = getAlertBackgroundColor(),
            onDismissRequest = {
                showAlertDialog = false
            },
            confirmButton = {
                TextButton(onClick = { showAlertDialog = false }) {
                    Text("OK")
                }
            }
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
            scaffoldState = scaffoldState,
            noResultsText = stringResource(id = R.string.no_results_found_search)
        ) {
            SearchResults(
                lazyListState = lazyListState,
                lazyPagingItems = lazyPagingItems,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun SearchResults(
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<UiData>,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> }
) {
    LazyColumn(
        state = lazyListState
    ) {
        items(lazyPagingItems) { uiData: UiData? ->
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
                        style = MaterialTheme.typography.body2,
                        text = "End of search results."
                    )
                }
                // TODO: can we somehow insert a footer for when we know there are more results but network failed?
                // TODO: loading more footer?
                else -> {
                    // Null. Do nothing.
                }
            }
        }
    }
}

// TODO: include Group/Person etc
@Composable
private fun ArtistCard(
    artist: UiArtist,
    onArtistClick: (UiArtist) -> Unit = {}
) {
    ClickableListItem(
        onClick = { onArtistClick(artist) },
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
        ) {

            Text(
                text = artist.name,
                modifier = Modifier.fillMaxWidth()
            )

            if (artist.disambiguation != null) {
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = "(${artist.disambiguation})",
                    color = getSubTextColor(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// region Previews

class ArtistPreviewParameterProvider : PreviewParameterProvider<UiArtist> {
    override val values = sequenceOf(
        UiArtist(
            id = "1",
            name = "artist name",
            sortName = "sort name should not be seen",
        ),
        UiArtist(
            id = "2",
            type = "Group",
            name = "wow, this artist name is so long it will wrap around the screen",
            sortName = "sort name should not be seen",
            disambiguation = "blah, blah, blah, some really long text that forces wrapping",
            country = "JP",
            lifeSpan = LifeSpan(
                begin = "2020-10-10"
            )
        )
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ArtistCardPreview(
    @PreviewParameter(ArtistPreviewParameterProvider::class) artist: UiArtist
) {
    MusicBrainzJetpackComposeTheme {
        Surface {
            ArtistCard(artist)
        }
    }
}
// endregion
