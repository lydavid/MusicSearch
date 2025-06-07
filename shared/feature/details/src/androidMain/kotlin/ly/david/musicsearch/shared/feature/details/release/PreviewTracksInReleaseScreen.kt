package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewTracksInReleaseScreen() {
    PreviewTheme {
        Surface {
            val items = MutableStateFlow(
                PagingData.from(
                    listOf(
                        ListSeparator(
                            id = "1",
                            text = "7\" Vinyl 1",
                        ),
                        TrackListItemModel(
                            id = "1111",
                            position = 1,
                            number = "A1",
                            title = "Track name",
                            length = 295000,
                            mediumId = 1,
                        ),
                        ListSeparator(
                            id = "2",
                            text = "7\" Vinyl 2",
                        ),
                        TrackListItemModel(
                            id = "2222",
                            position = 1,
                            number = "B1",
                            title = "Should not be shown",
                            length = 199000,
                            mediumId = 2,
                        ),
                        ListSeparator(
                            id = "3",
                            text = "7\" Vinyl 3",
                        ),
                        TrackListItemModel(
                            id = "3333",
                            position = 1,
                            number = "C1",
                            title = "Another track name",
                            length = 199000,
                            mediumId = 3,
                        ),
                    ),
                ),
            )

            TracksByReleaseUi(
                lazyPagingItems = items.collectAsLazyPagingItems(),
                collapsedMediumIds = setOf(2),
            )
        }
    }
}
