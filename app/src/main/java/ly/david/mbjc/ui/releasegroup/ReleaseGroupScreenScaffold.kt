package ly.david.mbjc.ui.releasegroup

import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch

private enum class ReleaseGroupTab(val title: String) {
    RELEASES("Releases"),
    STATS("Stats"),
}

/**
 * Equivalent of a screen like: https://musicbrainz.org/release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756
 *
 * Displays a list of releases under this release group.
 */
@Composable
fun ReleaseGroupScreenScaffold(
    releaseGroupId: String,
    onReleaseClick: (String) -> Unit = {},
    onBack: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    var titleState by rememberSaveable { mutableStateOf("") }
    var subtitleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(ReleaseGroupTab.RELEASES) }
    var searchText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBarWithSearch(
                title = titleState,
                subtitle = subtitleState,
                onBack = onBack,
                dropdownMenuItems = {
                    DropdownMenuItem(onClick = {
                        context.lookupInBrowser(MusicBrainzResource.RELEASE_GROUP, releaseGroupId)
                        closeMenu()
                    }) {
                        Text("Open in browser")
                    }
                },
                tabsTitles = ReleaseGroupTab.values().map { it.title },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ReleaseGroupTab.values()[it] },
                showSearchIcon = selectedTab == ReleaseGroupTab.RELEASES,
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                },
            )
        },
    ) { innerPadding ->

        when (selectedTab) {
            ReleaseGroupTab.RELEASES -> {
                ReleasesByReleaseGroupScreen(
                    modifier = Modifier.padding(innerPadding),
                    releaseGroupId = releaseGroupId,
                    onTitleUpdate = { title, subtitle ->
                        titleState = title
                        subtitleState = subtitle
                    },
                    scaffoldState = scaffoldState,
                    onReleaseClick = onReleaseClick,
                    searchText = searchText
                )
            }
            ReleaseGroupTab.STATS -> {
                Text(text = "nothing")
            }
        }
    }
}
