package ly.david.musicsearch.shared.feature.details.event

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.event.EventScaffoldModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

// region Previews
@PreviewLightDark
@Composable
internal fun PreviewEventDetailsUi() {
    PreviewTheme {
        Surface {
            EventDetailsUi(
                event = EventScaffoldModel(
                    id = "e1",
                    name = "Some Place",
                    type = "Festival",
                    time = "13:00",
                    cancelled = true,
                    lifeSpan = LifeSpanUiModel(
                        begin = "2022-01-01",
                        end = "2022-12-10",
                        ended = true,
                    ),
                ),
            )
        }
    }
}
// endregion
