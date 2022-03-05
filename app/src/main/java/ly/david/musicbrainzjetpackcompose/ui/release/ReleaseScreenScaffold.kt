package ly.david.musicbrainzjetpackcompose.ui.release

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
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

/**
 * Equivalent of a screen like: https://musicbrainz.org/release/f171e0ae-bea8-41e6-bb41-4c7af7977f50
 *
 * Displays the tracks/recordings for this release.
 */
@Composable
fun ReleaseScreenScaffold(
    releaseId: String,
    onBack: () -> Unit,
    onRecordingClick: (String) -> Unit = {}
) {

    var titleState by rememberSaveable { mutableStateOf("") }
    var subtitleState by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                title = titleState,
                subtitle = subtitleState,
                onBack = onBack,
                openInBrowser = {
                    context.lookupInBrowser(MusicBrainzResource.RELEASE, releaseId)
                },
            )
        },
    ) { innerPadding ->
        TracksInReleaseScreen(
            modifier = Modifier.padding(innerPadding),
            releaseId = releaseId,
            onTitleUpdate = { title, subtitle ->
                titleState = title
                subtitleState = subtitle
            },
            onRecordingClick = onRecordingClick
        )
    }
}
