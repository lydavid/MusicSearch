package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toDisplayTime
import ly.david.musicsearch.shared.domain.recording.RecordingDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun RecordingDetailsUi(
    recording: RecordingDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = detailsTabUiState.lazyListState,
    ) {
        item {
            recording.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.recording))
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

                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )
            }
        }
        urlsSection(
            urls = recording.urls,
        )
    }
}
