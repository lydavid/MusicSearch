package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.SMALL_IMAGE_SIZE
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
internal fun CollectionListItem(
    collection: CollectionListItemModel,
    modifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
    onClick: CollectionListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = collection.name,
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = collection.fontWeight,
            )
        },
        modifier = modifier.clickable { onClick(collection) },
        colors = colors,
        supportingContent = {
            // We currently don't support adding descriptions. Descriptions are not returned from MB's API either.
            Text(
                text = collection.description,
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = collection.fontWeight,
            )
        },
        leadingContent = {
            EntityIcon(
                entity = collection.entity,
                modifier = Modifier.size(SMALL_IMAGE_SIZE.dp),
            )
        },
        trailingContent = {
            Row {
                // TODO: not accurate for remote collections we have not clicked into yet
                Text(
                    text = "${collection.cachedEntityCount}",
                    modifier = Modifier.padding(end = 8.dp),
                    style = TextStyles.getCardBodyTextStyle(),
                    fontWeight = collection.fontWeight,
                )
                if (collection.isRemote) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = Icons.Outlined.Cloud,
                        contentDescription = null,
                    )
                }
            }
        },
    )
}
