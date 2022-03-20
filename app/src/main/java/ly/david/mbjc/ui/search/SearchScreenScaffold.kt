package ly.david.mbjc.ui.search

import android.content.res.Configuration
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ly.david.mbjc.ui.Destination
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
internal fun SearchScreenScaffold(
    openDrawer: () -> Unit = {},
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> }
) {

    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { ScrollableTopAppBar(title = "Search Music Brainz", openDrawer = openDrawer) },
    ) {
        SearchMusicBrainzScreen(
            scaffoldState = scaffoldState,
            onItemClick = onItemClick,
        )
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
