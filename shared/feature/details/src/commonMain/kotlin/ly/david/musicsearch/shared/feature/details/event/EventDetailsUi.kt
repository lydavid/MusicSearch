package ly.david.musicsearch.shared.feature.details.event

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.event.EventDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.image.LargeImage
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
internal fun EventDetailsUi(
    event: EventDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onCollapseExpandExternalLinks: () -> Unit = {},
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = detailsTabUiState.lazyListState,
    ) {
        event.run {
            item {
                if (filterText.isBlank()) {
                    LargeImage(
                        url = imageMetadata.largeUrl,
                        placeholderKey = imageMetadata.databaseId.toString(),
                        onClick = onImageClick,
                    )
                }

                ListSeparatorHeader(text = strings.informationHeader(strings.event))
                detailsTabUiState.numberOfImages?.ifNotNull {
                    TextWithHeading(
                        heading = strings.numberOfImages,
                        text = "$it",
                        filterText = filterText,
                    )
                }
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
            }

            item {
                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )
            }

            // TODO: set list
            //  api for this seems like some kind markdown?

            urlsSection(
                urls = urls,
                collapsed = detailsTabUiState.isExternalLinksCollapsed,
                onCollapseExpand = onCollapseExpandExternalLinks,
            )
        }
    }
}
