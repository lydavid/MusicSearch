package ly.david.ui.collections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.listitem.CollectionListItemModel
import ly.david.ui.common.EntityIcon
import ly.david.ui.core.SMALL_IMAGE_SIZE
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun CollectionListItem(
    collection: CollectionListItemModel,
    modifier: Modifier = Modifier,
    onClick: CollectionListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = collection.name,
                style = TextStyles.getCardBodyTextStyle()
            )
        },
        modifier = modifier.clickable { onClick(collection) },
        supportingContent = {
            // TODO: if we add more content to this column, it messes up any BottomModalSheet
            //  problem seems to appear when list is of certain length (eg. 4 items) regardless of this
            Text(
                text = collection.description,
                style = TextStyles.getCardBodyTextStyle()
            )
        },
        leadingContent = {
            // TODO: don't need it when viewed from within an entity's page

            EntityIcon(
                entity = collection.entity,
                modifier = Modifier.size(SMALL_IMAGE_SIZE.dp)
            )
        },
        trailingContent = {
            Row {
                if (collection.isRemote) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = Icons.Outlined.Cloud,
                        contentDescription = null
                    )
                }
                // TODO: count isn't accurate right now
//                Text(
//                    text = collection.entityCount.toString(),
//                    style = TextStyles.getCardBodyTextStyle()
//                )
            }
        }
    )
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            CollectionListItem(
                collection = CollectionListItemModel(
                    id = "3048448c-0605-494a-9e9f-c1a0521906f1",
                    isRemote = true,
                    name = "My collection with a very long title",
                    description = "Some songs",
                    entity = MusicBrainzEntity.RECORDING,
                    entityCount = 9999,
                    entityIds = listOf(
                        "1b1e4b65-9b1a-48cd-8e3a-b4824f15bf0c",
                        "b437fbda-9c32-4078-afa2-1afb98ff0d74"
                    )
                )
            )
        }
    }
}
