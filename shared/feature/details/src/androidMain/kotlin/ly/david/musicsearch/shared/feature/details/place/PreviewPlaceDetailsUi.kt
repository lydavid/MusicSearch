package ly.david.musicsearch.shared.feature.details.place

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewPlaceDetailsUi() {
    PreviewTheme {
        Surface {
            PlaceDetailsTabUi(
                place = PlaceDetailsModel(
                    id = "p1",
                    name = "Some Place",
                    type = "Venue",
                    address = "123 Fake St",
                    coordinates = CoordinatesUiModel(
                        -123.4567,
                        123.4567,
                    ),
                    lifeSpan = LifeSpanUiModel(
                        begin = "2022-01-01",
                        end = "2022-12-10",
                        ended = true,
                    ),
                    area = AreaListItemModel(
                        id = "a1",
                        "Area",
                        "that one",
                    ),
                ),
            )
        }
    }
}
