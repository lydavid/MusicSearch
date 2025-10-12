package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.icons.CheckCircle
import ly.david.musicsearch.ui.common.icons.Cloud
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.SMALL_IMAGE_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun CollectionListItem(
    collection: CollectionListItemModel,
    modifier: Modifier = Modifier,
    query: String = "",
    onClick: (String) -> Unit = {},
    enabled: Boolean = true,
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
) {
    val listItemModifier = if (enabled) {
        modifier.combinedClickable(
            onClick = {
                onClick(collection.id)
            },
            onLongClick = {
                if (!collection.isRemote) {
                    onSelect(collection.id)
                }
            },
        )
    } else {
        modifier
    }
    ListItem(
        headlineContent = {
            HighlightableText(
                text = collection.name,
                highlightedText = query,
                fontWeight = collection.fontWeight,
            )
        },
        modifier = listItemModifier,
        colors = listItemColors(
            isSelected = isSelected,
            enabled = enabled,
        ),
        // We currently don't support adding descriptions. Descriptions are not returned from MB's API either.
        leadingContent = {
            val sizeModifier = Modifier
                .size(SMALL_IMAGE_SIZE.dp)
            val finalModifier = if (collection.isRemote) {
                sizeModifier
            } else {
                sizeModifier.clickable {
                    onSelect(collection.id)
                }
            }

            if (isSelected) {
                Icon(
                    modifier = finalModifier,
                    imageVector = CustomIcons.CheckCircle,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "selected",
                )
            } else {
                EntityIcon(
                    entityType = collection.entity,
                    modifier = finalModifier,
                )
            }
        },
        trailingContent = {
            Row {
                // TODO: not accurate for remote collections we have not clicked into yet
                //  or haven't loaded all
                Text(
                    text = "${collection.cachedEntityCount}",
                    modifier = Modifier.padding(end = 8.dp),
                    style = TextStyles.getCardBodyTextStyle(),
                    fontWeight = collection.fontWeight,
                )
                if (collection.isRemote) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = CustomIcons.Cloud,
                        contentDescription = null,
                    )
                }
                if (collection.containsEntity) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = CustomIcons.CheckCircle,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null,
                    )
                }
            }
        },
    )
}
