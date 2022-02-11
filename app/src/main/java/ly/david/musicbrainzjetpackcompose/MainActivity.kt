package ly.david.musicbrainzjetpackcompose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.musicbrainzjetpackcompose.ui.trial.ExposedDropdownMenuBoxExample

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp("Android")
        }
    }
}

//https://musicbrainz.org/ws/2/
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

@ExperimentalMaterialApi
@Composable
fun MainApp(name: String) {
    MusicBrainzJetpackComposeTheme {

        var text by rememberSaveable { mutableStateOf("") }

        var selectedOption by remember { mutableStateOf(QueryResources.ARTIST) }

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
            Row(
//                modifier = Modifier.fillMaxWidth()
            ) {
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
                            // TODO: query api
                            Log.d("debug", "MainApp: querying /${selectedOption.queryText} for $text")
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

        }

        // A surface container using the 'background' color from the theme
//        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
//        }
    }
}

@Composable
fun MyTopBar() {
//    TopAppBar(
//        title = { Text("Search Artists") },
//        navigationIcon = {
//            Row(modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.End) {
//                IconButton(
//                    onClick = {
//                        Log.d("ss", "MyTopBar: clicked!")
//                    }
//                ) {
//                    Icon(imageVector = Icons.Default.Search, contentDescription = "Query for artists")
//                }
//            }
//
//        }
//    )
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.primary,
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        Row(
            modifier = Modifier.padding(AppBarDefaults.ContentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Text(
//                "Search Artists",
//                fontSize = TextUnit.Unspecified
//            )

            ProvideTextStyle(value = MaterialTheme.typography.h6) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = { Text("Search Artists") }
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Query for artists")
            }
        }
    }
}

@Composable
fun MyTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = AppBarDefaults.TopAppBarElevation
) {
    AppBar(
        backgroundColor,
        contentColor,
        elevation,
        AppBarDefaults.ContentPadding,
        RectangleShape,
        modifier
    ) {


        Row(
            Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.h6) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = title
                )
            }
        }

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Row(
                Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                content = actions
            )
        }

        if (navigationIcon == null) {
            Spacer(Modifier.width(16.dp - 4.dp))
        } else {
            Row(
                Modifier
                    .fillMaxHeight()
                    .width(72.dp - 4.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                    content = navigationIcon
                )
            }
        }
    }
}

@Composable
private fun AppBar(
    backgroundColor: Color,
    contentColor: Color,
    elevation: Dp,
    contentPadding: PaddingValues,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .height(56.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainApp("Android")
}

@ExperimentalMaterialApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    MainApp("Android")
}
