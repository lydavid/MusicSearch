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
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.EventListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeadingRes
import ly.david.mbjc.ui.common.listitem.InformationListSeparatorHeader
import ly.david.mbjc.ui.common.listitem.LifeSpanText
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun EventDetailsScreen(
    modifier: Modifier = Modifier,
    event: EventListItemModel,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            event.run {
                InformationListSeparatorHeader(R.string.event)
                type?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.type, text = it)
                }
                LifeSpanText(lifeSpan = lifeSpan)
                time?.ifNotNullOrEmpty {
                    TextWithHeadingRes(headingRes = R.string.time, text = it)
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
                event = EventListItemModel(
                    id = "e1",
                    name = "Some Place",
                    type = "Festival",
                    time = "13:00",
                    cancelled = true,
                    lifeSpan = LifeSpan(
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

