package ly.david.musicsearch.ui.common.area

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNull
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.common.toFlagEmoji
import ly.david.musicsearch.core.models.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.core.models.getLifeSpanForDisplay
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
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
) {
    ListItem(
        headlineContent = {
            val flags = area.countryCodes?.joinToString { it.toFlagEmoji() }
            val areaName = if (flags.isNullOrEmpty()) {
                area.name
            } else {
                flags.transformThisIfNotNullOrEmpty { "$it " } + area.name
            }
            Text(
                text = areaName,
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        supportingContent = {
            Column {
                DisambiguationText(disambiguation = area.disambiguation)

                val type = area.type
                if (showType && !type.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = type,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                area.lifeSpan.ifNotNull {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it.getLifeSpanForDisplay(),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        modifier = modifier.clickable {
            onAreaClick(area)
        },
        trailingContent = {
            area.date.ifNotNullOrEmpty {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = it,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
    )
}
