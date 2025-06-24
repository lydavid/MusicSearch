package ly.david.musicsearch.shared.feature.spotify.history

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.SpotifyHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Search
import ly.david.musicsearch.ui.common.theme.LocalStrings

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
            endIcon = CustomIcons.Search,
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
                endIcon = CustomIcons.Search,
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
                endIcon = CustomIcons.Search,
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
