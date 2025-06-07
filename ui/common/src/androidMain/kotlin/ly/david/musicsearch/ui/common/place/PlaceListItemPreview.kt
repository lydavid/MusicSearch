package ly.david.musicsearch.ui.common.place

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewPlaceListItem() {
    PreviewWithSharedElementTransition {
        PlaceListItem(
            place = PlaceListItemModel(
                id = "2",
                name = "Place Name",
                address = "123 Fake St",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewPlaceListItemAllInfo() {
    PreviewWithSharedElementTransition {
        PlaceListItem(
            place = PlaceListItemModel(
                id = "ed121457-87f6-4df9-a24b-d3f1bab1fdad",
                name = "Sony Music Studios",
                disambiguation = "NYC, closed 2007",
                type = "Studio",
                address = "460 W. 54th St., at 10th Avenue, Manhatten, NY",
                lifeSpan = LifeSpanUiModel(
                    begin = "1993",
                    end = "2007-08",
                    ended = true,
                ),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewPlaceListItemVisited() {
    PreviewWithSharedElementTransition {
        PlaceListItem(
            place = PlaceListItemModel(
                id = "ed121457-87f6-4df9-a24b-d3f1bab1fdad",
                name = "Sony Music Studios",
                disambiguation = "NYC, closed 2007",
                type = "Studio",
                address = "460 W. 54th St., at 10th Avenue, Manhatten, NY",
                lifeSpan = LifeSpanUiModel(
                    begin = "1993",
                    end = "2007-08",
                    ended = true,
                ),
                visited = true,
            ),
        )
    }
}
