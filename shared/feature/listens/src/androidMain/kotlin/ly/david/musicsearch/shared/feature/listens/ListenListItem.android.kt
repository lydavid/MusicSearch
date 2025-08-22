package ly.david.musicsearch.shared.feature.listens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewListenListItem() {
    PreviewWithSharedElementTransition {
        Surface {
            ListenListItem(
                listen = ListenListItemModel(
                    id = "1",
                    name = "絶絶絶絶対聖域",
                    formattedArtistCredits = "ano feat. 幾田りら",
                    listenedAt = kotlin.time.Instant.fromEpochMilliseconds(1755655177000),
                ),
            )
        }
    }
}
