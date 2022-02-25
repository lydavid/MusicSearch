package ly.david.musicbrainzjetpackcompose.ui.artist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.ui.common.ScrollableTopAppBar

//        listOf("Overview", "Releases", "Recordings", "Works", "Events", "Recordings", "Aliases", "Tags", "Details")
enum class ArtistTab(val title: String) {
    OVERVIEW("Overview"),
    RELEASE_GROUPS("Release Groups"),
    RELEASES("Releases"),
}


// TODO: string

// TODO: use lookup on artist first
// TODO: get all release group for artist in second tab? don't make api call until user swipes to it for the first time
//  experience is slightly different from web though, where their list of release groups is the main focus
@Composable
fun ArtistScreenScaffold(
    artist: Artist,
    onReleaseGroupClick: (String) -> Unit = {},
    onBack: () -> Unit
) {

    var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.OVERVIEW) }

    val browseReleaseGroupsState = rememberLazyListState()

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                title = artist.name,
                onBack = onBack,
                tabsTitle = ArtistTab.values().map { it.title },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ArtistTab.values()[it] }
            )
        },
    ) { innerPadding ->

        when(selectedTab) {
            ArtistTab.OVERVIEW -> {
                Text("just me")
            }
            ArtistTab.RELEASE_GROUPS -> {
                ReleaseGroupsByArtistScreen(
                    modifier = Modifier.padding(innerPadding),
                    artistId = artist.id,
                    state = browseReleaseGroupsState,
                    onReleaseGroupClick = onReleaseGroupClick
                )
            }
            ArtistTab.RELEASES -> {
                Text(text = "Nothing yet!")
            }
        }
    }
}
