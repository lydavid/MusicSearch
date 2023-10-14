package ly.david.ui.spotify

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.topappbar.ScrollableTopAppBar
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
fun SpotifyScaffold(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    var metadata: SpotifyMetadata by remember { mutableStateOf(SpotifyMetadata()) }

    SpotifyBroadcastReceiver {
        metadata = it
    }

    SpotifyScaffold(
        metadata = metadata,
        modifier = modifier,
        onBack = onBack,
        searchMusicBrainz = searchMusicBrainz,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SpotifyScaffold(
    metadata: SpotifyMetadata,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    val strings = LocalStrings.current
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                onBack = onBack,
                title = strings.spotify,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        SpotifyScreen(
            metadata = metadata,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = searchMusicBrainz,
        )
    }
}

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewSpotifyScaffold() {
    PreviewTheme {
        SpotifyScaffold(
            metadata = SpotifyMetadata(
                artistName = "Artist",
                albumName = "Album",
                trackName = "Track",
            ),
        )
    }
}
// endregion
