package ly.david.musicsearch.ui.common.area

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.theme.TextStyles

/**
 * Also used for release event cards since their Destination is also an Area.
 */
@Composable
fun AreaListItem(
    area: AreaListItemModel,
    modifier: Modifier = Modifier,
    showType: Boolean = true,
    onAreaClick: AreaListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
) {
    ListItem(
        headlineContent = {
            val flags = area.countryCodes.joinToString { it.toFlagEmoji() }
            val areaName = if (flags.isEmpty()) {
                area.name
            } else {
                flags.transformThisIfNotNullOrEmpty { "$it " } + area.name
            }
            Text(
                text = areaName,
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = area.fontWeight,
            )
        },
        colors = listItemColors(isSelected = isSelected),
        modifier = modifier.combinedClickable(
            onClick = {
                onAreaClick(area)
            },
            onLongClick = {
                onSelect(area.id)
            },
        ),
        supportingContent = {
            Column {
                DisambiguationText(
                    disambiguation = area.disambiguation,
                    fontWeight = area.fontWeight,
                )

                val type = area.type
                if (showType && !type.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = type,
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = area.fontWeight,
                    )
                }

                area.lifeSpan.ifNotNull {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it.getLifeSpanForDisplay(),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = area.fontWeight,
                    )
                }
            }
        },
        leadingContent = {
            ThumbnailImage(
                url = "",
                placeholderKey = "",
                placeholderIcon = MusicBrainzEntity.AREA.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(area.id)
                    },
                isSelected = isSelected,
            )
        },
        trailingContent = {
            area.date.ifNotNullOrEmpty {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = it,
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = area.fontWeight,
                    )
                }
            }
        },
    )
}
