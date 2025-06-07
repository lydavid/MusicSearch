package ly.david.musicsearch.ui.common.series

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewSeriesListItem() {
    PreviewWithSharedElementTransition {
        SeriesListItem(
            series = SeriesListItemModel(
                id = "1",
                name = "series name",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSeriesListItemAllInfo() {
    PreviewWithSharedElementTransition {
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

@PreviewLightDark
@Composable
internal fun PreviewSeriesListItemVisited() {
    PreviewWithSharedElementTransition {
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
