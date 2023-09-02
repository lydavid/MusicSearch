package ly.david.ui.spotify

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.spotify.SpotifyMetadata
import ly.david.ui.common.R
import ly.david.ui.common.component.ClickableItem
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun SpotifyScreen(
    metadata: SpotifyMetadata,
    modifier: Modifier = Modifier,
    searchMusicBrainz: (query: String, id: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
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
    Card(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.spotify_tutorial),
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
    metadata.artistName.ifNotNullOrEmpty { artistName ->
        ClickableItem(
            title = artistName,
            subtitle = stringResource(id = R.string.search_x, artistName),
            endIcon = Icons.Default.Search,
            onClick = {
                searchMusicBrainz(
                    "\"$artistName\"",
                    MusicBrainzEntity.ARTIST
                )
            }
        )

        metadata.albumName.ifNotNullOrEmpty { albumName ->
            ClickableItem(
                title = albumName,
                subtitle = stringResource(id = R.string.search_x_by_x, albumName, artistName),
                endIcon = Icons.Default.Search,
                onClick = {
                    searchMusicBrainz(
                        "\"$albumName\" artist:\"$artistName\"",
                        MusicBrainzEntity.RELEASE_GROUP,
                    )
                }
            )
        }

        metadata.trackName.ifNotNullOrEmpty { trackName ->
            ClickableItem(
                title = trackName,
                subtitle = stringResource(id = R.string.search_x_by_x, trackName, artistName),
                endIcon = Icons.Default.Search,
                onClick = {
                    searchMusicBrainz(
                        "\"$trackName\" artist:\"$artistName\"",
                        MusicBrainzEntity.RECORDING,
                    )
                }
            )
        }
    }
}

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewSpotifyScreenEmpty() {
    PreviewTheme {
        Surface {
            SpotifyScreen(
                metadata = SpotifyMetadata(
                    artistName = "Artist",
                    albumName = "Album",
                    trackName = "Track",
                )
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewSpotifyScreenWithData() {
    PreviewTheme {
        Surface {
            SpotifyScreen(metadata = SpotifyMetadata())
        }
    }
}
// endregion
