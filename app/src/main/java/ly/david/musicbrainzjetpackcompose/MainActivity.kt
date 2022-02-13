package ly.david.musicbrainzjetpackcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ControlCamera
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
import ly.david.musicbrainzjetpackcompose.ui.discovery.ArtistCard
import ly.david.musicbrainzjetpackcompose.ui.discovery.ExposedDropdownMenuBoxExample
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp(viewModel)
        }
    }
}

// area, artist, event, genre, instrument, label, place, recording, release, release-group, series, work, url
enum class QueryResources(val displayText: String, val queryText: String) {
    AREA("Area", "area"),
    ARTIST("Artist", "artist"),
    EVENT("Event", "event"),
    GENRE("Genre", "genre"),
    INSTRUMENT("Instrument", "instrument"),
    LABEL("Label", "label"),
    PLACE("Place", "place"),
    RECORDING("Recording", "recording"),
    RELEASE("Release", "release"),
    RELEASE_GROUP("Release Group", "release_group"),
    SERIES("Series", "series"),
    WORK("Work", "work"),
    URL("URL", "url")
}

@Composable
internal fun MainApp(viewModel: MainViewModel) {
    MusicBrainzJetpackComposeTheme {

        var text by rememberSaveable { mutableStateOf("") }

        var selectedOption by remember { mutableStateOf(QueryResources.ARTIST) }

//        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search Artists") },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.ControlCamera, contentDescription = "Query for artists")
                        }
                    },
                    backgroundColor = Color.White
                )
            }
        ) {
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
                    items(viewModel.artists) { artist ->
                        ArtistCard(artist = artist)
                    }
                }
            }

        }
    }
}

// TODO: we should be previewing specific components, rather than the entire app
//  these components should not have reference to the viewmodel
//@ExperimentalMaterialApi
//@Preview(showBackground = true)
//@Composable
//internal fun DefaultPreview() {
//    MainApp("Android")
//}
//
//@ExperimentalMaterialApi
//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//internal fun DarkPreview() {
//    MainApp("Android")
//}
