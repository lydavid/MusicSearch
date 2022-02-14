package ly.david.musicbrainzjetpackcompose.ui.discovery

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.MainViewModel
import ly.david.musicbrainzjetpackcompose.QueryResources
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
internal fun SearchScreen(
    onClickArtist: (String) -> Unit = {},
    viewModel: MainViewModel = viewModel()
) {

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
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
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
                    // TODO: could pass entire artist data if necessary
                    Log.d("Remove This", "MainApp: clicked on artist with id=$it")
                    onClickArtist(it)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
internal fun SearchScreenPreview() {
    MusicBrainzJetpackComposeTheme {
        SearchScreen()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun SearchScreenDarkPreview() {
    MusicBrainzJetpackComposeTheme {
        SearchScreen()
    }
}
