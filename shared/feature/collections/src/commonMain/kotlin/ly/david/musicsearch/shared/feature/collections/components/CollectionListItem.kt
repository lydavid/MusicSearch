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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.UNKNOWN
import ly.david.musicsearch.shared.domain.listitem.CollectionContainsEntities
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.icons.CheckCircle
import ly.david.musicsearch.ui.common.icons.CheckIndeterminateCircle
import ly.david.musicsearch.ui.common.icons.Circle
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
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = collection.fontWeight)) {
                        append(collection.name)
                    }
                },
                highlightedText = query,
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
                val cachedEntityCount = collection.cachedEntityCount
                val hasUnknown = collection.isRemote && collection.remoteEntityCount != cachedEntityCount
                val countText = "$cachedEntityCount" + if (hasUnknown) {
                    " + $UNKNOWN"
                } else {
                    ""
                }
                Text(
                    text = countText,
                    modifier = Modifier.padding(end = 8.dp),
                    style = TextStyles.getCardBodyTextStyle(),
                )
                if (collection.isRemote) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = CustomIcons.Cloud,
                        contentDescription = null,
                    )
                }
                when (collection.containsEntities) {
                    null -> {
                        // Nothing
                    }

                    CollectionContainsEntities.None -> {
                        Icon(
                            modifier = Modifier.padding(end = 4.dp),
                            imageVector = CustomIcons.Circle,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = null,
                        )
                    }

                    CollectionContainsEntities.Some -> {
                        Icon(
                            modifier = Modifier.padding(end = 4.dp),
                            imageVector = CustomIcons.CheckIndeterminateCircle,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                        )
                    }

                    CollectionContainsEntities.All -> {
                        Icon(
                            modifier = Modifier.padding(end = 4.dp),
                            imageVector = CustomIcons.CheckCircle,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                        )
                    }
                }
            }
        },
    )
}
