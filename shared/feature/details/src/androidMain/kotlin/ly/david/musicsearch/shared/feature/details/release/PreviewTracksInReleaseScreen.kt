package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewTracksInReleaseScreen() {
    PreviewTheme {
        Surface {
            val items = MutableStateFlow(
                PagingData.from(
                    listOf(
                        ListSeparator(
                            id = "separator1",
                            text = "7\" Vinyl 1",
                        ),
                        TrackListItemModel(
                            id = "1",
                            position = 1,
                            number = "A1",
                            title = "Track name",
                            length = 295000,
                        ),
                        ListSeparator(
                            id = "separator2",
                            text = "7\" Vinyl 2",
                        ),
                        TrackListItemModel(
                            id = "2",
                            position = 1,
                            number = "B1",
                            title = "Another track name",
                            length = 199000,
                        ),
                    ),
                ),
            )

            TracksByReleaseScreen(
                lazyPagingItems = items.collectAsLazyPagingItems(),
            )
        }
    }
}
