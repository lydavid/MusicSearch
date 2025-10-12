package ly.david.musicsearch.shared.feature.graph

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewArtistCollaborationGraphUi() {
    PreviewTheme {
        Surface {
            ArtistCollaborationGraphUi(
                edges = persistentListOf(
                    GraphEdge(
                        x0 = -7.010541039928112,
                        y0 = 148.53020774179376,
                        x1 = 34.442058589085114,
                        y1 = -98.70221015951988,
                    ),
                    GraphEdge(
                        x0 = -87.41917955713517,
                        y0 = 120.97761778046068,
                        x1 = -47.58660614527603,
                        y1 = -111.3471010505625,
                    ),
                    GraphEdge(
                        x0 = -87.41917955713517,
                        y0 = 120.97761778046068,
                        x1 = 107.57529725372356,
                        y1 = -59.45777853885758,
                    ),
                ),
                nodes = persistentListOf(
                    GraphNode(
                        id = "6114677c-fa8f-4d87-960a-3f1169aaef89",
                        name = "TK from 凛として時雨",
                        entityType = MusicBrainzEntityType.ARTIST,
                        radius = 26.0,
                        x = -7.010541039928112,
                        y = 148.53020774179376,
                    ),
                    GraphNode(
                        id = "80b3cb83-b7a3-4f79-ad42-8325cefb3626",
                        name = "キタニタツヤ",
                        entityType = MusicBrainzEntityType.ARTIST,
                        radius = 25.0,
                        x = -87.41917955713517,
                        y = 120.97761778046068,
                    ),
                    GraphNode(
                        id = "51999312-1044-4f91-92b0-ed23974929ca",
                        name = "melt",
                        entityType = MusicBrainzEntityType.RECORDING,
                        radius = 24.0,
                        x = 34.442058589085114,
                        y = -98.70221015951988,
                    ),
                    GraphNode(
                        id = "9a70f454-1da3-4570-b592-33e5f8713d28",
                        name = "ナイトルーティーン",
                        entityType = MusicBrainzEntityType.RECORDING,
                        radius = 25.0,
                        x = 107.57529725372356,
                        y = -59.45777853885758,
                    ),
                    GraphNode(
                        id = "d4a15774-05b7-4f58-97b1-2bdf4a10d8d5",
                        name = "ナイトルーティーン",
                        entityType = MusicBrainzEntityType.RECORDING,
                        radius = 25.0,
                        x = -47.58660614527603,
                        y = -111.3471010505625,
                    ),
                ),
                filterText = "",
            )
        }
    }
}
