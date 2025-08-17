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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TextStyles

/**
 * Also used for release event cards since their Destination is also an Area.
 */
@Composable
fun AreaListItem(
    area: AreaListItemModel,
    modifier: Modifier = Modifier,
    showType: Boolean = true,
    showIcon: Boolean = true,
    showEditCollection: Boolean = true,
    onAreaClick: AreaListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    val leadingContent: @Composable (() -> Unit)? =
        if (showIcon) {
            {
                ThumbnailImage(
                    url = "",
                    imageId = null,
                    placeholderIcon = MusicBrainzEntityType.AREA.getIcon(),
                    modifier = Modifier
                        .clickable {
                            onSelect(area.id)
                        },
                    isSelected = isSelected,
                )
            }
        } else {
            null
        }

    ListItem(
        headlineContent = {
            val flags = area.countryCodes.joinToString { it.toFlagEmoji() }
            val fullName = buildAnnotatedString {
                flags.ifNotNullOrEmpty { append("$it ") }
                append(area.getAnnotatedName())
            }
            Text(
                text = fullName,
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
        leadingContent = leadingContent,
        trailingContent = {
            if (showEditCollection) {
                AddToCollectionIconButton(
                    entityListItemModel = area,
                    onClick = onEditCollectionClick,
                )
            }

            // Only for release events
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
