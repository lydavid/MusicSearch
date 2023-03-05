package ly.david.mbjc.ui.collections

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.network.MusicBrainzResource

/**
 * A single collection.
 * Displays all items in this collection.
 */
@Composable
internal fun CollectionScaffold(
    collectionId: String,
    modifier: Modifier = Modifier,
    onEntityClick: (entity: MusicBrainzResource, id: String) -> Unit = { _, _ -> },
) {

    // TODO: list of X by collection

    Text(text = collectionId)
}
