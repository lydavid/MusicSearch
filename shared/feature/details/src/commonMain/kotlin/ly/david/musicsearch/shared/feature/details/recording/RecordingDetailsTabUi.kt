package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
internal fun RecordingDetailsTabUi(
    recording: RecordingDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    val strings = LocalStrings.current

    DetailsTabUi(
        detailsModel = recording,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        onCollapseExpandAliases = onCollapseExpandAliases,
        entityInfoSection = {
            length?.ifNotNull {
                TextWithHeading(
                    heading = strings.length,
                    text = it.toDisplayTime(),
                    filterText = filterText,
                )
            }
            firstReleaseDate?.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.firstReleaseDate,
                    text = it,
                    filterText = filterText,
                )
            }
            isrcs?.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.isrc,
                    text = it.joinToString(", "),
                    filterText = filterText,
                )
            }
        },
    )
}
