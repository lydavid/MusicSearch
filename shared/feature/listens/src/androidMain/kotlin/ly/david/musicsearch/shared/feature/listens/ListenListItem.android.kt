package ly.david.musicsearch.shared.feature.listens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.AliasType
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewListenListItem() {
    PreviewWithSharedElementTransition {
        Surface {
            ListenListItem(
                listen = ListenListItemModel(
                    listenedAtMs = 1755655177000,
                    recordingMessybrainzId = "bf2c5a43-19d8-46f7-8131-df986ed24845",
                    username = "user",
                    name = "絶絶絶絶対聖域",
                    formattedArtistCredits = "ano feat. 幾田りら",
                    durationMs = 213868,
                    aliases = persistentListOf(
                        BasicAlias(
                            name = "ZeZeZeZettai Seiiki",
                            locale = "en",
                            type = AliasType.RECORDING_NAME,
                            isPrimary = true,
                        ),
                    ),
                ),
            )
        }
    }
}
