package ly.david.mbjc.ui.artist

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.ScrollableTopAppBar
import ly.david.mbjc.ui.common.lookupInBrowser

//        listOf("Overview", "Releases", "Recordings", "Works", "Events", "Recordings", "Aliases", "Tags", "Details")
enum class ArtistTab(val title: String) {
//    OVERVIEW("Overview"),
    RELEASE_GROUPS("Release Groups"),
    RELEASES("Releases"),
}

@Composable
fun ArtistScreenScaffold(
    artistId: String,
    onReleaseGroupClick: (String) -> Unit = {},
    onBack: () -> Unit
) {

    var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.RELEASE_GROUPS) }
    var artistName by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()
    val browseReleaseGroupsState = rememberLazyListState()

    // TODO: "Filter" is for selecting chips like album type
    var isSearchAndFilterMode by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {

            // TODO: expand out from the icon
            AnimatedVisibility(
                visible = isSearchAndFilterMode,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }
                )
            ) {

                // TODO: when returning, focus is in front of search text
                //  most apps seems to not bring up the keyboard when returning
                LaunchedEffect(Unit) {
                    // TODO: only do them when first clicking on search icon
                    focusRequester.requestFocus()
                }

                BackHandler {
                    isSearchAndFilterMode = false
                    searchText = ""
                }

                Column {
                    TextField(
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .fillMaxWidth(),
                        maxLines = 1,
                        singleLine = true,
//                        shape = RoundedCornerShape(32.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            IconButton(onClick = {
                                isSearchAndFilterMode = false
                                searchText = ""
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        placeholder = { Text("Search release groups") },
                        trailingIcon = {
                            if (searchText.isEmpty()) return@TextField
                            IconButton(onClick = {
                                searchText = ""
                                focusRequester.requestFocus()
                            }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search field.")
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                focusManager.clearFocus()
                            }
                        ),
                        value = searchText,
                        onValueChange = {
                            searchText = it
                        }
                    )

                    // TODO: Filters

                    Divider()
                }
            }

            AnimatedVisibility(
                visible = !isSearchAndFilterMode,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> -fullWidth }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> -fullWidth }
                )
            ) {
                ScrollableTopAppBar(
                    title = artistName,
                    onBack = onBack,
                    mainAction = {
                        if (selectedTab == ArtistTab.RELEASE_GROUPS) {
                            IconButton(onClick = {
                                isSearchAndFilterMode = true
                            }) {
                                Icon(Icons.Default.Search, "")
                            }
                        }
                    },
                    dropdownMenuItems = {
                        DropdownMenuItem(onClick = {
                            context.lookupInBrowser(MusicBrainzResource.ARTIST, artistId)
                        }) {
                            Text("Open in browser")
                        }

                        // TODO:
                        if (selectedTab == ArtistTab.RELEASE_GROUPS) {
                            DropdownMenuItem(onClick = {
                                // TODO: dropdown or something with what to sort by
                                Log.d("Remove This", "ArtistScreenScaffold: Only for this tab!")
                            }) {
                                Text("Sort")
                            }

                            // TODO: good for debugging, but could give users some details of how many release groups are in db, network
                            DropdownMenuItem(onClick = {
                                Log.d("Remove This", "ArtistScreenScaffold: ee")
                            }) {
                                Text("Stats")
                            }

                            DropdownMenuItem(onClick = {
                                Log.d("Remove This", "ArtistScreenScaffold: ee")
                            }) {
                                Text("Refresh")
                            }
                        }
                    },
                    tabsTitle = ArtistTab.values().map { it.title },
                    selectedTabIndex = selectedTab.ordinal,
                    onSelectTabIndex = { selectedTab = ArtistTab.values()[it] }
                )
            }

        },
    ) { innerPadding ->

        when (selectedTab) {
            ArtistTab.RELEASE_GROUPS -> {
                ReleaseGroupsByArtistScreen(
                    modifier = Modifier.padding(innerPadding),
                    artistId = artistId,
                    searchText = searchText,
                    state = browseReleaseGroupsState,
                    scaffoldState = scaffoldState,
                    onReleaseGroupClick = onReleaseGroupClick,
                    onTitleUpdate = {
                        artistName = it
                    }
                )
            }
            ArtistTab.RELEASES -> {
                Text(text = "Nothing yet!")
            }
        }
    }
}

