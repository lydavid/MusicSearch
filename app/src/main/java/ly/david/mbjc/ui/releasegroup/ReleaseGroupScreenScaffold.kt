package ly.david.mbjc.ui.releasegroup

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ly.david.mbjc.data.MusicBrainzResource
import ly.david.mbjc.ui.common.ScrollableTopAppBar
import ly.david.mbjc.ui.common.lookupInBrowser

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
                    context.lookupInBrowser(MusicBrainzResource.RELEASE_GROUP, releaseGroupId)
                },
            )
        },
    ) { innerPadding ->
        ReleasesByReleaseGroupScreen(
            modifier = Modifier.padding(innerPadding),
            releaseGroupId = releaseGroupId,
            onTitleUpdate = { title, subtitle ->
                titleState = title
                subtitleState = subtitle
            },
            onReleaseClick = onReleaseClick
        )
    }
}
