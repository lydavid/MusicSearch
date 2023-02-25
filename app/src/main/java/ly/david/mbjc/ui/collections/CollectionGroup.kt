package ly.david.mbjc.ui.collections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.SMALL_COVER_ART_SIZE
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

// TODO: decide whether user should click into a collection group
// or we can just display all collections in a list, separated by ListSeparator
// kinda prefer option 2, so it's simpler to filter on collection titles
//  and doesn't need a disabled state for collection types
/**
 *
 * - based on whether user has any collection of that type, they will be disabled
 * - says how many collections of each type user has
 */

@Composable
internal fun CollectionGroup(
    musicBrainzResource: MusicBrainzResource
) {
    Card {

        Row {

            ResourceIcon(
                resource = musicBrainzResource,
                modifier = Modifier.size(SMALL_COVER_ART_SIZE.dp)
            )

            Text(text = "Artist collections")
        }
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            CollectionGroup(
                MusicBrainzResource.ARTIST
            )
        }
    }
}
