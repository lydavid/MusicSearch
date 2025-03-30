package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupDetailsUi() {
    PreviewTheme {
        Surface {
            ReleaseGroupDetailsUi(
                releaseGroup = ReleaseGroupDetailsModel(
                    id = "bdaeec2d-94f1-46b5-91f3-340ec6939c66",
                    name = "Under Pressure",
                    primaryType = "Single",
                    firstReleaseDate = "1981-10",
                    wikipediaExtract = WikipediaExtract(
                        extract = "\"Under Pressure\" is a song by the British rock band Queen and singer David Bowie.",
                        wikipediaUrl = "https://en.wikipedia.org/wiki/Under_Pressure",
                    ),
                ),
            )
        }
    }
}
