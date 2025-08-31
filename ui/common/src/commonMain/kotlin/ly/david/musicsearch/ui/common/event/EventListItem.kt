package ly.david.musicsearch.ui.common.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun EventListItem(
    event: EventListItemModel,
    modifier: Modifier = Modifier,
    onEventClick: EventListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    val strings = LocalStrings.current

    ListItem(
        headlineContent = {
            Column {
                event.run {
                    Text(
                        text = getAnnotatedName(),
                        style = TextStyles.getCardBodyTextStyle(),
                        fontWeight = event.fontWeight,
                    )

                    type.ifNotEmpty {
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
                        time.ifNotEmpty { time ->
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
                        if (cancelled) {
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = buildString {
                                    append(" ").takeIf { lifeSpanText.isNotEmpty() || time.isNotEmpty() }
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
        modifier = modifier.combinedClickable(
            onClick = { onEventClick(event) },
            onLongClick = { onSelect(event.id) },
        ),
        colors = listItemColors(isSelected = isSelected),
        leadingContent = {
            ThumbnailImage(
                url = event.imageUrl.orEmpty(),
                imageId = event.imageId,
                placeholderIcon = MusicBrainzEntityType.EVENT.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(event.id)
                    },
                isSelected = isSelected,
            )
        },
        trailingContent = {
            AddToCollectionIconButton(
                entityListItemModel = event,
                onClick = onEditCollectionClick,
            )
        },
    )
}
