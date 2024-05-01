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
import ly.david.musicsearch.android.feature.spotify.history.SpotifyUiEvent
import ly.david.musicsearch.android.feature.spotify.history.SpotifyUiState
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.history.SpotifyHistory
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.component.ClickableItem
import ly.david.ui.common.topappbar.ScrollableTopAppBar
import ly.david.ui.core.LocalStrings
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun LegacySpotifyUi(
    state: SpotifyUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    LegacySpotifyUi(
        spotifyHistory = state.spotifyHistory,
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
private fun LegacySpotifyUi(
    spotifyHistory: SpotifyHistory?,
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
            spotifyHistory = spotifyHistory,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            searchMusicBrainz = searchMusicBrainz,
        )
    }
}

@Composable
internal fun SpotifyContent(
    spotifyHistory: SpotifyHistory?,
    modifier: Modifier = Modifier,
    searchMusicBrainz: (query: String, id: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        item {
            if (spotifyHistory == null) {
                TutorialBanner()
            } else {
                SpotifySearchLinks(
                    spotifyHistory = spotifyHistory,
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
    spotifyHistory: SpotifyHistory?,
    searchMusicBrainz: (query: String, id: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    val strings = LocalStrings.current

    spotifyHistory?.artistName.ifNotNullOrEmpty { artistName ->
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

        spotifyHistory?.albumName.ifNotNullOrEmpty { albumName ->
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

        spotifyHistory?.trackName.ifNotNullOrEmpty { trackName ->
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
        LegacySpotifyUi(
            spotifyHistory = SpotifyHistory(
                trackId = "1",
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
            LegacySpotifyUi(spotifyHistory = null)
        }
    }
}
// endregion
