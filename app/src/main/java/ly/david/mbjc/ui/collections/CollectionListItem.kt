package ly.david.mbjc.ui.collections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.SMALL_COVER_ART_SIZE
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun CollectionListItem(
    collectionListItemModel: CollectionListItemModel
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row {
            ResourceIcon(
                resource = collectionListItemModel.entity,
                modifier = Modifier.size(SMALL_COVER_ART_SIZE.dp)
            )

            Text(text = collectionListItemModel.name)
        }
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            CollectionListItem(
                collectionListItemModel = CollectionListItemModel(
                    id = "0",
                    name = "My collection",
                    entity = MusicBrainzResource.RECORDING
                )
            )
        }
    }
}
