package ly.david.mbjc.ui.artist

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch

//        listOf("Overview", "Releases", "Recordings", "Works", "Events", "Recordings", "Aliases", "Tags", "Details")
enum class ArtistTab(val title: String) {
    //    OVERVIEW("Overview"),
    RELEASE_GROUPS("Release Groups"),
    RELEASES("Releases"),
    STATS("Stats")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreenScaffold(
    artistId: String,
    onReleaseGroupClick: (String) -> Unit = {},
    onBack: () -> Unit,
) {

    var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.RELEASE_GROUPS) }
    var artistName by rememberSaveable { mutableStateOf("") }

//    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }

    var searchText by rememberSaveable { mutableStateOf("") }

    var isSorted by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithSearch(
                onBack = onBack,
                title = artistName,
                showSearchIcon = selectedTab == ArtistTab.RELEASE_GROUPS,
                dropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text("Open in browser") },
                        onClick = {
                        context.lookupInBrowser(MusicBrainzResource.ARTIST, artistId)
                        closeMenu()
                    })

                    if (selectedTab == ArtistTab.RELEASE_GROUPS) {
                        DropdownMenuItem(
                            text = {
                                Text(if (isSorted) "Un-sort" else "Sort")
                            },
                            onClick = {
                            closeMenu()
                            // TODO: disclaimer when turning on sort if we have not gotten all release groups
                            isSorted = !isSorted
                        })

                        DropdownMenuItem(
                            text =  {
                                Text("Refresh")
                            },
                            onClick = {
                            Log.d("Remove This", "ArtistScreenScaffold: ee")
                            closeMenu()
                        })
                    }
                },
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                },
                tabsTitles = ArtistTab.values().map { it.title },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ArtistTab.values()[it] }
            )
        },
    ) { innerPadding ->

        Surface(modifier = Modifier.fillMaxSize()) {
            when (selectedTab) {
                ArtistTab.RELEASE_GROUPS -> {
                    ReleaseGroupsByArtistScreen(
                        modifier = Modifier.padding(innerPadding),
                        artistId = artistId,
                        searchText = searchText,
                        isSorted = isSorted,
                        snackbarHostState = snackbarHostState,
                        onReleaseGroupClick = onReleaseGroupClick,
                        onTitleUpdate = {
                            artistName = it
                        }
                    )
                }
                ArtistTab.RELEASES -> {
                    Text(text = "Nothing yet!")
                }
                ArtistTab.STATS -> {
                    ArtistStatsScreen(artistId = artistId)
                }
            }
        }
    }
}

