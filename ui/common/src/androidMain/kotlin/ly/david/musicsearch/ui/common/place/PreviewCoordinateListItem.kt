package ly.david.musicsearch.ui.common.place

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import ly.david.musicsearch.ui.common.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCoordinateListItem() {
    PreviewTheme {
        Surface {
            CoordinateListItem(
                coordinates = CoordinatesUiModel(
                    longitude = -73.98905,
                    latitude = 40.76688,
                ),
            )
        }
    }
}
