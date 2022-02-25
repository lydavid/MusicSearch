package ly.david.musicbrainzjetpackcompose.ui.search

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import kotlinx.coroutines.launch
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.data.LifeSpan
import ly.david.musicbrainzjetpackcompose.ui.common.ClickableCard
import ly.david.musicbrainzjetpackcompose.ui.common.ScrollableTopAppBar
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.musicbrainzjetpackcompose.ui.theme.getSubTextColor

@Composable
internal fun SearchScreenScaffold(
    onArtistClick: (Artist) -> Unit = {},
    openDrawer: () -> Unit = {},
) {

    val lazyListState: LazyListState = rememberLazyListState()

    Scaffold(
        topBar = { ScrollableTopAppBar(title = "Search Artists", openDrawer = openDrawer) },

        ) {
        SearchScreen(onArtistClick, lazyListState)
    }
}

@Composable
private fun SearchScreen(
    onArtistClick: (Artist) -> Unit = {},
    state: LazyListState,
    viewModel: SearchViewModel = viewModel()
) {
    var text by rememberSaveable { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf(QueryResources.ARTIST) }

    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    val pagingItems: LazyPagingItems<Artist> = viewModel.artists.collectAsLazyPagingItems()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                modifier = Modifier.weight(1f),
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
                                // TODO: error
                                Log.d("Remove This", "SearchScreen: can't be empty!!")
                            } else {
                                viewModel.query.value = text
                                state.scrollToItem(0)
                                focusManager.clearFocus()
                            }
                        }
                    }
                ),
                trailingIcon = {
                    if (text.isEmpty()) return@TextField
                    IconButton(onClick = { text = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search field.")
                    }
                },
                onValueChange = { newText ->
                    text = newText
                }
            )

            // TODO: this doesn't fill rest of screen despite weight 1f
            ExposedDropdownMenuBox(
                modifier = Modifier.weight(1f),
                options = QueryResources.values().toList(),
                selectedOption = selectedOption,
                onSelectOption = {
                    selectedOption = it
                }
            )
        }


        LazyColumn(
            state = state
        ) {
//            item {
//                val results = viewModel.totalFoundResults.value
//                if (results != 0) {
//                    Text(text = "Found $results results for \"${viewModel.queryString}\"")
//                }
//            }
//
//            items(viewModel.artists) { artist ->
//                ArtistCard(artist = artist) {
//                    onArtistClick(it)
//                }
//            }
            itemsIndexed(pagingItems) { _, artist ->
                if (artist == null) return@itemsIndexed
                ArtistCard(artist = artist) {
                    onArtistClick(it)
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
    ClickableCard(
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

private val testArtist = Artist(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    type = "Group",
    typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
    score = 77,
    name = "Paracoccidioidomicosisproctitissarcomucosis paracoccidioidomicosisproctitissarcomucosisevenlonger",
    sortName = "Tsukuyomi",
    disambiguation = "blah, some really long text that forces wrapping",
    country = "JP",
    lifeSpan = LifeSpan(
        begin = "2020-10-10"
    )
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ArtistCardPreview() {
    MusicBrainzJetpackComposeTheme {
        ArtistCard(testArtist)
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
