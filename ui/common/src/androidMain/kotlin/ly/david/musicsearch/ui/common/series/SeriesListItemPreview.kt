package ly.david.musicsearch.ui.common.series

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewSeriesListItem() {
    PreviewWithTransitionAndOverlays {
        SeriesListItem(
            series = SeriesListItemModel(
                id = "1",
                name = "series name",
            ),
            filterText = "",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSeriesListItemAllInfo() {
    PreviewWithTransitionAndOverlays {
        SeriesListItem(
            series = SeriesListItemModel(
                id = "1",
                name = "series name",
                disambiguation = "that one",
                type = "Tour",
            ),
            filterText = "se",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSeriesListItemVisited() {
    PreviewWithTransitionAndOverlays {
        SeriesListItem(
            series = SeriesListItemModel(
                id = "1",
                name = "series name",
                disambiguation = "that one",
                type = "Tour",
                visited = true,
            ),
            filterText = "",
        )
    }
}
