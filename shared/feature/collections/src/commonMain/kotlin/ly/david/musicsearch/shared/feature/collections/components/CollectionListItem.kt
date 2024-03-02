package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.ui.common.EntityIcon
import ly.david.ui.core.SMALL_IMAGE_SIZE
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun CollectionListItem(
    collection: CollectionListItemModel,
    modifier: Modifier = Modifier,
    onClick: CollectionListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = collection.name,
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        modifier = modifier.clickable { onClick(collection) },
        supportingContent = {
            // TODO: if we add more content to this column, it messes up any BottomModalSheet
            //  problem seems to appear when list is of certain length (eg. 4 items) regardless of this
            Text(
                text = collection.description,
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        leadingContent = {
            // TODO: don't need it when viewed from within an entity's page

            EntityIcon(
                entity = collection.entity,
                modifier = Modifier.size(SMALL_IMAGE_SIZE.dp),
            )
        },
        trailingContent = {
            Row {
                if (collection.isRemote) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = Icons.Outlined.Cloud,
                        contentDescription = null,
                    )
                }
                // TODO: count isn't accurate right now
//                Text(
//                    text = collection.entityCount.toString(),
//                    style = TextStyles.getCardBodyTextStyle()
//                )
            }
        },
    )
}
