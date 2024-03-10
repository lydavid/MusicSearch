package ly.david.musicsearch.shared.feature.details.area.details

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.musicsearch.shared.feature.details.area.AreaDetailsScreen
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewAreaDetailsScreen() {
    PreviewTheme {
        Surface {
            AreaDetailsScreen(
                area = AreaScaffoldModel(
                    id = "88f49821-05a3-3bbc-a24b-bbd6b918c07b",
                    name = "Czechoslovakia",
                    type = "Country",
                    lifeSpan = LifeSpanUiModel(
                        begin = "1918-10-28",
                        end = "1992-12-31",
                        ended = true,
                    ),
                    countryCodes = listOf("XC"),
                ),
            )
        }
    }
}
// endregion
