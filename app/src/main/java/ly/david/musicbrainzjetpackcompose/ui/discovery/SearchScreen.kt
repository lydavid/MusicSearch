package ly.david.musicbrainzjetpackcompose.ui.discovery

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.QueryResources
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.data.LifeSpan
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
internal fun SearchScreenScaffold(
    onArtistClick: (Artist) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Artists") },
            )
        }
    ) {
        SearchScreen(onArtistClick)
    }
}

@Composable
private fun SearchScreen(
    onArtistClick: (Artist) -> Unit = {},
    viewModel: SearchViewModel = viewModel()
) {
    // TODO: this updates live after a successful search result...
    var text by rememberSaveable { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf(QueryResources.ARTIST) }

    val focusManager = LocalFocusManager.current

    Column {
        Row {
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
                        viewModel.queryArtists(text)
                        Log.d("debug", "MainApp: querying /${selectedOption.queryText} for $text")
                        focusManager.clearFocus()
                    }
                ),
                onValueChange = { newText ->
                    text = newText
                }
            )

            ExposedDropdownMenuBoxExample(
                modifier = Modifier.weight(1f),
                options = QueryResources.values().toList(),
                selectedOption = selectedOption,
                onSelectOption = {
                    selectedOption = it
                }
            )
        }

        LazyColumn {
            item {
                val results = viewModel.totalFoundResults.value
                if (results != 0) {
                    Text(text = "Found $results results for \"$text\"")
                }
            }

            items(viewModel.artists) { artist ->
                ArtistCard(artist = artist) {
                    onArtistClick(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ArtistCard(
    artist: Artist,
    onArtistClick: (Artist) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onArtistClick(artist) },
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {

            Text(
                text = artist.name,
                modifier = Modifier.fillMaxWidth()
            )

            if (artist.disambiguation != null) {
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Text(
                    text = "(${artist.disambiguation})",
                    color = Color.Gray,
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

@Preview(showBackground = true)
@Composable
internal fun ArtistCardPreview() {
    MusicBrainzJetpackComposeTheme {
        ArtistCard(testArtist)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ArtistCardDarkPreview() {
    MusicBrainzJetpackComposeTheme {
        ArtistCard(testArtist)
    }
}

@Preview(showBackground = true)
@Composable
internal fun SearchScreenPreview() {
    MusicBrainzJetpackComposeTheme {
        SearchScreenScaffold()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SearchScreenDarkPreview() {
    MusicBrainzJetpackComposeTheme {
        SearchScreenScaffold()
    }
}
