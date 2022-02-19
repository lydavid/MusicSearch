package ly.david.musicbrainzjetpackcompose.ui.releasegroup

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.musicbrainzjetpackcompose.data.Release
import ly.david.musicbrainzjetpackcompose.ui.common.ScrollableTopAppBar

/**
 * Equivalent of a screen like: https://musicbrainz.org/release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756
 *
 * Displays a list of releases under this release group.
 */
@Composable
fun ReleaseGroupScreenScaffold(
    releaseGroupId: String,
    onReleaseClick: (Release) -> Unit = {},
) {

    var title by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { ScrollableTopAppBar(title = title) },
    ) { innerPadding ->
        ReleasesByReleaseGroupScreen(
            modifier = Modifier.padding(innerPadding),
            releaseGroupId = releaseGroupId,
            onTitleUpdate = { title = it },
            onReleaseClick = onReleaseClick
        )
    }
}
