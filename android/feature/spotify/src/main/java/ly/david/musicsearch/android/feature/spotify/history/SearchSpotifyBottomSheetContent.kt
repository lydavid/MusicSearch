package ly.david.musicsearch.android.feature.spotify.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.component.ClickableItem
import ly.david.ui.common.getIcon
import ly.david.ui.core.LocalStrings
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun SearchSpotifyBottomSheetContent(
    spotifyHistory: SpotifyHistoryListItemModel?,
    searchMusicBrainz: (query: String, id: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    val strings = LocalStrings.current

    spotifyHistory?.artistName.ifNotNullOrEmpty { artistName ->
        ClickableItem(
            title = strings.searchX(artistName),
            startIcon = MusicBrainzEntity.ARTIST.getIcon(),
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
                title = strings.searchXByX(
                    albumName,
                    artistName,
                ),
                startIcon = MusicBrainzEntity.RELEASE_GROUP.getIcon(),
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
                title = strings.searchXByX(
                    trackName,
                    artistName,
                ),
                startIcon = MusicBrainzEntity.RECORDING.getIcon(),
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
    Spacer(modifier = Modifier.padding(top = 64.dp))
}

@PreviewLightDark
@Composable
internal fun PreviewSearchSpotifyBottomSheetContent() {
    PreviewTheme {
        Surface {
            Column {
                SearchSpotifyBottomSheetContent(
                    spotifyHistory = SpotifyHistoryListItemModel(
                        id = "1",
                        trackName = "Take on Me",
                        artistName = "a-ha",
                        albumName = "Hunting High and Low",
                    ),
                )
            }
        }
    }
}
