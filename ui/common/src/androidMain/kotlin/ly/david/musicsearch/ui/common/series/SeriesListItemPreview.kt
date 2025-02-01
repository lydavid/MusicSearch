package ly.david.musicsearch.ui.common.series

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewSeriesListItem() {
    PreviewTheme {
        Surface {
            SeriesListItem(
                series = SeriesListItemModel(
                    id = "1",
                    name = "series name",
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSeriesListItemAllInfo() {
    PreviewTheme {
        Surface {
            SeriesListItem(
                series = SeriesListItemModel(
                    id = "1",
                    name = "series name",
                    disambiguation = "that one",
                    type = "Tour",
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSeriesListItemVisited() {
    PreviewTheme {
        Surface {
            SeriesListItem(
                series = SeriesListItemModel(
                    id = "1",
                    name = "series name",
                    disambiguation = "that one",
                    type = "Tour",
                    visited = true,
                ),
            )
        }
    }
}
