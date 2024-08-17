package ly.david.musicsearch.shared.feature.graph

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewArtistCollaborationGraphUi() {
    PreviewTheme {
        Surface {
            ArtistCollaborationGraphUi(
                edges = listOf(
                    GraphEdge(
                        x0 = -7.010541039928112,
                        y0 = 148.53020774179376,
                        x1 = 34.442058589085114,
                        y1 = -98.70221015951988,
                    ),
                    GraphEdge(
                        x0 = -7.010541039928112,
                        y0 = 148.53020774179376,
                        x1 = 107.57529725372356,
                        y1 = -59.45777853885758,
                    ),
                    GraphEdge(
                        x0 = -7.010541039928112,
                        y0 = 148.53020774179376,
                        x1 = -47.58660614527603,
                        y1 = -111.3471010505625,
                    ),
                    GraphEdge(
                        x0 = -87.41917955713517,
                        y0 = 120.97761778046068,
                        x1 = 107.57529725372356,
                        y1 = -59.45777853885758,
                    ),
                    GraphEdge(
                        x0 = -87.41917955713517,
                        y0 = 120.97761778046068,
                        x1 = -47.58660614527603,
                        y1 = -111.3471010505625,
                    ),
                    GraphEdge(
                        x0 = -7.010541039928112,
                        y0 = 148.53020774179376,
                        x1 = 34.442058589085114,
                        y1 = -98.70221015951988,
                    ),
                    GraphEdge(
                        x0 = -87.41917955713517,
                        y0 = 120.97761778046068,
                        x1 = 107.57529725372356,
                        y1 = -59.45777853885758,
                    ),
                    GraphEdge(
                        x0 = -7.010541039928112,
                        y0 = 148.53020774179376,
                        x1 = 107.57529725372356,
                        y1 = -59.45777853885758,
                    ),
                    GraphEdge(
                        x0 = -87.41917955713517,
                        y0 = 120.97761778046068,
                        x1 = -47.58660614527603,
                        y1 = -111.3471010505625,
                    ),
                    GraphEdge(
                        x0 = -7.010541039928112,
                        y0 = 148.53020774179376,
                        x1 = -47.58660614527603,
                        y1 = -111.3471010505625,
                    ),
                ),
                nodes = listOf(
                    GraphNode(
                        id = "b2de971f-b3d6-409f-9ed0-ecfc09e8b4e3",
                        name = "suis",
                        entity = MusicBrainzEntity.ARTIST,
                        radius = 26.0,
                        x = -7.010541039928112,
                        y = 148.53020774179376,
                    ),
                    GraphNode(
                        id = "80b3cb83-b7a3-4f79-ad42-8325cefb3626",
                        name = "キタニタツヤ",
                        entity = MusicBrainzEntity.ARTIST,
                        radius = 25.0,
                        x = -87.41917955713517,
                        y = 120.97761778046068,
                    ),
                    GraphNode(
                        id = "58576a03-f302-4f68-9a97-113a6afc753f",
                        name = "星めぐる詩",
                        entity = MusicBrainzEntity.RECORDING,
                        radius = 24.0,
                        x = 34.442058589085114,
                        y = -98.70221015951988,
                    ),
                    GraphNode(
                        id = "9a70f454-1da3-4570-b592-33e5f8713d28",
                        name = "ナイトルーティーン",
                        entity = MusicBrainzEntity.RECORDING,
                        radius = 25.0,
                        x = 107.57529725372356,
                        y = -59.45777853885758,
                    ),
                    GraphNode(
                        id = "d4a15774-05b7-4f58-97b1-2bdf4a10d8d5",
                        name = "ナイトルーティーン",
                        entity = MusicBrainzEntity.RECORDING,
                        radius = 25.0,
                        x = -47.58660614527603,
                        y = -111.3471010505625,
                    ),
                ),
            )
        }
    }
}
