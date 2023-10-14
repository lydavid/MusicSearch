package ly.david.mbjc.ui.recording.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.data.core.common.ifNotNull
import ly.david.musicsearch.data.core.common.ifNotNullOrEmpty
import ly.david.musicsearch.data.core.common.toDisplayTime
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.recording.RecordingScaffoldModel
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
internal fun RecordingDetailsScreen(
    recording: RecordingScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState
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
                        text = it.joinToString("\n"),
                        filterText = filterText,
                    )
                }

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

// region Previews
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            RecordingDetailsScreen(
                recording = RecordingScaffoldModel(
                    id = "132a508b-624a-4f1d-b61f-f6616121bab5",
                    name = "プライド革命",
                    length = 235000,
                    isrcs = listOf(
                        "JPX401500068"
                    )
                )
            )
        }
    }
}
// endregion
