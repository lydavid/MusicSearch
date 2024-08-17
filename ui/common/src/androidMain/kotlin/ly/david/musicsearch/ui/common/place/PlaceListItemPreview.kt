package ly.david.musicsearch.ui.common.place

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

internal class PlacePreviewParameterProvider : PreviewParameterProvider<PlaceListItemModel> {
    override val values = sequenceOf(
        PlaceListItemModel(
            id = "2",
            name = "Place Name",
            address = "123 Fake St",
        ),
        PlaceListItemModel(
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
        PlaceListItemModel(
            id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
            name = "日本武道館",
            type = "Indoor arena",
            address = "〒102-8321 東京都千代田区北の丸公園2-3",
            lifeSpan = LifeSpanUiModel(begin = "1964-10-03"),
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(PlacePreviewParameterProvider::class) place: PlaceListItemModel,
) {
    PreviewTheme {
        Surface {
            PlaceListItem(place)
        }
    }
}
