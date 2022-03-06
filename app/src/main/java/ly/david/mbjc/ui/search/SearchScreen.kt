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
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import kotlinx.coroutines.launch
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.ScrollableTopAppBar
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.mbjc.ui.theme.getAlertBackgroundColor
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun SearchScreenScaffold(
    openDrawer: () -> Unit = {},
    onArtistClick: (String) -> Unit = {},
    viewModel: SearchViewModel = viewModel()
) {

    val lazyListState: LazyListState = rememberLazyListState()
    val pagingItems: LazyPagingItems<Artist> = viewModel.artists.collectAsLazyPagingItems()

    Scaffold(
        topBar = { ScrollableTopAppBar(title = "Search Artists", openDrawer = openDrawer) },
    ) {
        SearchScreen(
            state = lazyListState,
            pagingItems = pagingItems,
            onSearch = { query ->
                viewModel.query.value = query
            },
            onArtistClick = onArtistClick,
        )
    }
}

@Composable
private fun SearchScreen(
    state: LazyListState,
    pagingItems: LazyPagingItems<Artist>,
    onSearch: (String) -> Unit = {},
    onArtistClick: (String) -> Unit = {}
) {
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
                                onSearch(text)
                                state.scrollToItem(0)
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

        LazyColumn(
            state = state
        ) {
            items(pagingItems) { artist ->
                if (artist == null) return@items
                ArtistCard(artist = artist) {
                    onArtistClick(it.id)
                }
            }
            // TODO: if we're at the end of the list, can we show a message saying so?
        }
    }
}

// TODO: include Group/Person etc
@Composable
private fun ArtistCard(
    artist: Artist,
    onArtistClick: (Artist) -> Unit = {}
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

class ArtistPreviewParameterProvider : PreviewParameterProvider<Artist> {
    override val values = sequenceOf(
        Artist(
            id = "1",
            name = "artist name",
            sortName = "sort name should not be seen",
        ),
        Artist(
            id = "2",
            type = "Group",
            typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
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
    @PreviewParameter(ArtistPreviewParameterProvider::class) artist: Artist
) {
    MusicBrainzJetpackComposeTheme {
        Surface {
            ArtistCard(artist)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SearchScreenPreview() {
    MusicBrainzJetpackComposeTheme {
        SearchScreenScaffold()
    }
}
// endregion
