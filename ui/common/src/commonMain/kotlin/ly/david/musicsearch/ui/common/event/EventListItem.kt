package ly.david.musicsearch.ui.common.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
fun EventListItem(
    event: EventListItemModel,
    modifier: Modifier = Modifier,
    onEventClick: EventListItemModel.() -> Unit = {},
) {
    val strings = LocalStrings.current

    ListItem(
        headlineContent = {
            Column {
                event.run {
                    Text(
                        text = name,
                        style = TextStyles.getCardBodyTextStyle(),
                        fontWeight = event.fontWeight,
                    )

                    DisambiguationText(
                        disambiguation = disambiguation,
                        fontWeight = event.fontWeight,
                    )

                    type.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = event.fontWeight,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val lifeSpanText = lifeSpan.getLifeSpanForDisplay()
                        if (lifeSpanText.isNotEmpty()) {
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = lifeSpanText,
                                style = TextStyles.getCardBodySubTextStyle(),
                                fontWeight = event.fontWeight,
                            )
                        }
                        time.ifNotNullOrEmpty { time ->
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = buildString {
                                    append(" · ").takeIf { lifeSpanText.isNotEmpty() }
                                    append(time)
                                },
                                style = TextStyles.getCardBodySubTextStyle(),
                                fontWeight = event.fontWeight,
                            )
                        }
                        if (cancelled == true) {
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = buildString {
                                    append(" ").takeIf { lifeSpanText.isNotEmpty() || !time.isNullOrEmpty() }
                                    append("(${strings.cancelled})")
                                },
                                style = TextStyles.getCardBodySubTextStyle(),
                                fontWeight = event.fontWeight,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.clickable {
            onEventClick(event)
        },
    )
}
