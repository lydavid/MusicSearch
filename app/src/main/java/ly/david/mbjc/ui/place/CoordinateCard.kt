package ly.david.mbjc.ui.place

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import ly.david.mbjc.data.Coordinates
import ly.david.mbjc.data.formatForDisplay
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.showMap
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun CoordinateCard(
    context: Context,
    coordinates: Coordinates,
    label: String? = null
) {

    val text = coordinates.formatForDisplay() ?: return

    ClickableListItem(onClick = {
        context.showMap(coordinates, label)
    }) {
        // TODO: design, icon to indicate it deeplinks to maps
        Column {
            Text(text = text)
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CoordinateCardPreview() {
    PreviewTheme {
        Surface {
            CoordinateCard(
                context = LocalContext.current,
                coordinates = Coordinates(
                    longitude = -73.98905,
                    latitude = 40.76688
                )
            )
        }
    }
}
