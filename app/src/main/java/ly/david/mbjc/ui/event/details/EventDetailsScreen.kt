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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.domain.event.EventScaffoldModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.LifeSpanUiModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.ui.common.R
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.listitem.LifeSpanText
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.common.url.UrlsSection
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
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            event.run {
                InformationListSeparatorHeader(R.string.event)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.type,
                        text = it,
                        filterText = filterText,
                    )
                }
                LifeSpanText(lifeSpan = lifeSpan)
                time?.ifNotNullOrEmpty {
                    TextWithHeadingRes(
                        headingRes = R.string.time,
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
                            text = "(${stringResource(id = R.string.cancelled)})",
                            style = TextStyles.getCardBodyTextStyle(),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                // TODO: set list
                //  api for this seems like some kind markdown?

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
@ExcludeFromJacocoGeneratedReport
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
                        ended = true
                    ),
                )
            )
        }
    }
}
// endregion
