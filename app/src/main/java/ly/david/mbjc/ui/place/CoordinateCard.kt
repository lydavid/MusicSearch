package ly.david.mbjc.ui.place

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ly.david.mbjc.data.Coordinates
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.showMap

@Composable
internal fun CoordinateCard(
    context: Context,
    coordinates: Coordinates,
    label: String? = null
) {
    ClickableListItem(onClick = {
        context.showMap(coordinates, label)
    }) {
        // TODO: design, icon to indicate it deeplinks to maps
        Column {
            Text(text = coordinates.longitude.toString())
            Text(text = coordinates.latitude.toString())
        }
    }
}
