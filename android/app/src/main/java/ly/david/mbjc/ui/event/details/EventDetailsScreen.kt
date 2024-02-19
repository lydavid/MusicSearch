package ly.david.mbjc.ui.event.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.event.EventScaffoldModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.listitem.LifeSpanText
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.commonlegacy.url.UrlsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun EventDetailsScreen(
    event: EventScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            event.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.event))
                type?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                LifeSpanText(
                    lifeSpan = lifeSpan,
                    heading = strings.date,
                    beginHeading = strings.startDate,
                    endHeading = strings.endDate,
                    filterText = filterText,
                )
                time?.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.time,
                        text = it,
                        filterText = filterText,
                    )
                }
                if (cancelled == true) {
                    SelectionContainer {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            text = "(${strings.cancelled})",
                            style = TextStyles.getCardBodyTextStyle(),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }

                // TODO: set list
                //  api for this seems like some kind markdown?

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick,
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
            EventDetailsScreen(
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
