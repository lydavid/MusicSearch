package ly.david.musicsearch.shared.feature.listens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.paging.PagingData
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition
import kotlin.time.Instant

@PreviewLightDark
@Composable
internal fun PreviewListensUi() {
    PreviewWithSharedElementTransition {
        Surface {
            val listens = MutableStateFlow(
                PagingData.from(
                    data = listOf(
                        ListenListItemModel(
                            id = "1",
                            name = "絶絶絶絶対聖域",
                            formattedArtistCredits = "ano feat. 幾田りら",
                            listenedAt = Instant.fromEpochMilliseconds(1755655177000),
                        ),
                    ),
                ),
            )
            ListensUi(
                state = ListensUiState(
                    username = "user",
                    listensPagingDataFlow = listens,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiNoUsername() {
    PreviewWithSharedElementTransition {
        Surface {
            val listens = MutableStateFlow(
                PagingData.from(
                    data = emptyList<ListenListItemModel>(),
                ),
            )
            ListensUi(
                state = ListensUiState(
                    listensPagingDataFlow = listens,
                ),
            )
        }
    }
}
