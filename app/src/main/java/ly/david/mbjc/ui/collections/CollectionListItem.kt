package ly.david.mbjc.ui.collections

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
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.isRemote
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.SMALL_COVER_ART_SIZE
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun CollectionListItem(
    collection: CollectionListItemModel,
    modifier: Modifier = Modifier,
    onClick: CollectionListItemModel.() -> Unit = {}
) {
    ListItem(
        headlineText = {
            Text(
                text = collection.name,
                style = TextStyles.getCardBodyTextStyle()
            )
        },
        modifier = modifier.clickable { onClick(collection) },
        supportingText = {
            // TODO: if we add more content to this column, it messes up any BottomModalSheet
            //  problem seems to appear when list is of certain length (eg. 4 items) regardless of this
            Text(
                text = collection.description,
                style = TextStyles.getCardBodyTextStyle()
            )
        },
        leadingContent = {
            // TODO: consider a different way to indicate what collection this is
            //  and don't need it when viewed from within an entity's page

            ResourceIcon(
                resource = collection.entity,
                modifier = Modifier.size(SMALL_COVER_ART_SIZE.dp)
            )
        },
        trailingContent = {
            Row {
                if (collection.isRemote()) {
                    Icon(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = Icons.Outlined.Cloud,
                        contentDescription = null
                    )
                }
                Text(
                    text = collection.entityCount.toString(),
                    style = TextStyles.getCardBodyTextStyle()
                )
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
                    id = "0",
                    mbid = "abc123",
                    name = "My collection with a very long title",
                    description = "Some songs",
                    entity = MusicBrainzResource.RECORDING,
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
