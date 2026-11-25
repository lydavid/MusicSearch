package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.UNKNOWN
import ly.david.musicsearch.shared.domain.collection.PendingEdit
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
import ly.david.musicsearch.ui.common.text.getFontWeightWithPreference
import ly.david.musicsearch.ui.common.theme.SMALL_IMAGE_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.selectedItemsInCollection
import musicsearch.ui.common.generated.resources.selectedItemsNotInCollection
import musicsearch.ui.common.generated.resources.selectedItemsWillBeAddedToCollection
import musicsearch.ui.common.generated.resources.selectedItemsWillBeDeletedFromCollection
import musicsearch.ui.common.generated.resources.someSelectedItemsInCollection
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CollectionListItem(
    collection: CollectionListItemModel,
    modifier: Modifier = Modifier,
    boldUnvisited: Boolean = true,
    query: String = "",
    enabled: Boolean = true,
    isSelected: Boolean = false,
    onClick: (CollectionListItemModel) -> Unit = {},
    onSelect: ((String) -> Unit)? = null,
) {
    val itemClickModifier = when {
        enabled -> modifier.combinedClickable(
            onClick = {
                onClick(collection)
            },
            onLongClick = {
                if (!collection.isRemote && onSelect != null) {
                    onSelect(collection.id)
                }
            },
        )

        else -> modifier
    }
    ListItem(
        headlineContent = {
            HighlightableText(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = collection.getFontWeightWithPreference(boldUnvisited = boldUnvisited),
                        ),
                    ) {
                        append(collection.name)
                    }
                },
                highlightedText = query,
            )
        },
        modifier = itemClickModifier,
        colors = listItemColors(
            isSelected = isSelected,
            enabled = enabled,
        ),
        // We currently don't support adding descriptions. Descriptions are not returned from MB's API either.
        leadingContent = {
            val sizeModifier = Modifier
                .size(SMALL_IMAGE_SIZE.dp)
            val iconClickModifier = if (!collection.isRemote && onSelect != null) {
                sizeModifier.clickable {
                    onSelect(collection.id)
                }
            } else {
                sizeModifier
            }

            Box {
                collection.pendingEdit?.let { pendingEdit ->
                    VerticalDivider(
                        modifier = Modifier.height(SMALL_IMAGE_SIZE.dp),
                        color = if (pendingEdit == PendingEdit.Add) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                    )
                }

                if (isSelected) {
                    Icon(
                        modifier = iconClickModifier,
                        imageVector = CustomIcons.CheckCircle,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "selected",
                    )
                } else {
                    EntityIcon(
                        entityType = collection.entity,
                        modifier = iconClickModifier,
                    )
                }
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
                TristateIcon(collection)
            }
        },
    )
}

@Composable
private fun TristateIcon(collection: CollectionListItemModel) {
    val endModifier = Modifier.padding(end = 4.dp)
    when (collection.pendingEdit) {
        PendingEdit.Add -> {
            Icon(
                modifier = endModifier,
                imageVector = CustomIcons.CheckCircle,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(Res.string.selectedItemsWillBeAddedToCollection),
            )
        }

        PendingEdit.Remove -> {
            Icon(
                modifier = endModifier,
                imageVector = CustomIcons.Circle,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = stringResource(Res.string.selectedItemsWillBeDeletedFromCollection),
            )
        }

        null -> {
            when (collection.containsEntities) {
                null -> {
                    // Nothing
                }

                CollectionContainsEntities.None -> {
                    Icon(
                        modifier = endModifier,
                        imageVector = CustomIcons.Circle,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(Res.string.selectedItemsNotInCollection),
                    )
                }

                CollectionContainsEntities.Some -> {
                    Icon(
                        modifier = endModifier,
                        imageVector = CustomIcons.CheckIndeterminateCircle,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(Res.string.someSelectedItemsInCollection),
                    )
                }

                CollectionContainsEntities.All -> {
                    Icon(
                        modifier = endModifier,
                        imageVector = CustomIcons.CheckCircle,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(Res.string.selectedItemsInCollection),
                    )
                }
            }
        }
    }
}
