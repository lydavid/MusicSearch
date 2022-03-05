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
import androidx.compose.ui.platform.LocalContext
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzResource
import ly.david.musicbrainzjetpackcompose.ui.common.ScrollableTopAppBar
import ly.david.musicbrainzjetpackcompose.ui.common.lookupInBrowser

//        listOf("Overview", "Releases", "Recordings", "Works", "Events", "Recordings", "Aliases", "Tags", "Details")
enum class ArtistTab(val title: String) {
    OVERVIEW("Overview"),
    RELEASE_GROUPS("Release Groups"),
    RELEASES("Releases"),
}

@Composable
fun ArtistScreenScaffold(
    artistId: String,
    onReleaseGroupClick: (String) -> Unit = {},
    onBack: () -> Unit
) {

    var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.OVERVIEW) }
    var titleState by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val browseReleaseGroupsState = rememberLazyListState()

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                title = titleState,
                onBack = onBack,
                openInBrowser = {
                    context.lookupInBrowser(MusicBrainzResource.ARTIST, artistId)
                },
                tabsTitle = ArtistTab.values().map { it.title },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ArtistTab.values()[it] }
            )
        },
    ) { innerPadding ->

        when (selectedTab) {
            ArtistTab.OVERVIEW -> {
                ArtistOverviewScreen(
                    artistId = artistId,
                    onTitleUpdate = {
                        titleState = it
                    }
                )
            }
            ArtistTab.RELEASE_GROUPS -> {
                ReleaseGroupsByArtistScreen(
                    modifier = Modifier.padding(innerPadding),
                    artistId = artistId,
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
