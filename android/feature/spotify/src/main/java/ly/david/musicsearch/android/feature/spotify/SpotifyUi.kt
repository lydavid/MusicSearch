package ly.david.musicsearch.android.feature.spotify

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.component.ClickableItem
import ly.david.ui.common.topappbar.ScrollableTopAppBar
import ly.david.ui.core.LocalStrings
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun SpotifyUi(
    state: SpotifyUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    SpotifyUi(
        metadata = state.metadata,
        modifier = modifier,
        onBack = {
            eventSink(SpotifyUiEvent.NavigateUp)
        },
        searchMusicBrainz = { query, entity ->
            eventSink(
                SpotifyUiEvent.GoToSearch(
                    query = query,
                    entity = entity,
                ),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SpotifyUi(
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
        SpotifyContent(
            metadata = metadata,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = searchMusicBrainz,
        )
    }
}

@Composable
internal fun SpotifyContent(
    metadata: SpotifyMetadata,
    modifier: Modifier = Modifier,
    searchMusicBrainz: (query: String, id: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        item {
            val artistName = metadata.artistName
            if (artistName.isNullOrEmpty()) {
                TutorialBanner()
            } else {
                SpotifySearchLinks(
                    metadata = metadata,
                    searchMusicBrainz = searchMusicBrainz,
                )
            }
        }
    }
}

@Composable
private fun TutorialBanner() {
    val strings = LocalStrings.current

    Card(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Text(
            text = strings.spotifyTutorial,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = TextStyles.getCardBodySubTextStyle(),
        )
    }
}

@Composable
private fun SpotifySearchLinks(
    metadata: SpotifyMetadata,
    searchMusicBrainz: (query: String, id: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    val strings = LocalStrings.current

    metadata.artistName.ifNotNullOrEmpty { artistName ->
        ClickableItem(
            title = artistName,
            subtitle = strings.searchX(artistName),
            endIcon = Icons.Default.Search,
            onClick = {
                searchMusicBrainz(
                    "\"$artistName\"",
                    MusicBrainzEntity.ARTIST,
                )
            },
        )

        metadata.albumName.ifNotNullOrEmpty { albumName ->
            ClickableItem(
                title = albumName,
                subtitle = strings.searchXByX(
                    albumName,
                    artistName,
                ),
                endIcon = Icons.Default.Search,
                onClick = {
                    searchMusicBrainz(
                        "\"$albumName\" artist:\"$artistName\"",
                        MusicBrainzEntity.RELEASE_GROUP,
                    )
                },
            )
        }

        metadata.trackName.ifNotNullOrEmpty { trackName ->
            ClickableItem(
                title = trackName,
                subtitle = strings.searchXByX(
                    trackName,
                    artistName,
                ),
                endIcon = Icons.Default.Search,
                onClick = {
                    searchMusicBrainz(
                        "\"$trackName\" artist:\"$artistName\"",
                        MusicBrainzEntity.RECORDING,
                    )
                },
            )
        }
    }
}

// region Previews
@PreviewLightDark
@Composable
internal fun PreviewSpotifyUi() {
    PreviewTheme {
        SpotifyUi(
            metadata = SpotifyMetadata(
                artistName = "Artist",
                albumName = "Album",
                trackName = "Track",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSpotifyUiEmpty() {
    PreviewTheme {
        Surface {
            SpotifyUi(metadata = SpotifyMetadata())
        }
    }
}
// endregion
