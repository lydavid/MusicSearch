package ly.david.musicbrainzjetpackcompose.ui.release

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.musicbrainzjetpackcompose.ui.common.ScrollableTopAppBar

/**
 * Equivalent of a screen like: https://musicbrainz.org/release/f171e0ae-bea8-41e6-bb41-4c7af7977f50
 *
 * Displays the tracks/recordings for this release.
 */
@Composable
fun ReleaseScreenScaffold(
    releaseId: String,
//    onReleaseClick: (Release) -> Unit = {},
) {

    var title by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { ScrollableTopAppBar(title = title) },
    ) { innerPadding ->
        TracksInReleaseScreen(
            modifier = Modifier.padding(innerPadding),
            releaseId = releaseId,
            onTitleUpdate = { title = it },
//            onReleaseClick = onReleaseClick
        )
    }
}
