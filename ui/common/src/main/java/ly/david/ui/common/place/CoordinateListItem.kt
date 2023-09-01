package ly.david.ui.common.place

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.data.CoordinatesUiModel
import ly.david.data.common.showMap
import ly.david.data.formatForDisplay
import ly.david.ui.common.R
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun CoordinateListItem(
    context: Context,
    coordinates: CoordinatesUiModel,
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    val text = coordinates.formatForDisplay() ?: return

    ListItem(
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = Icons.Default.PinDrop,
                    contentDescription = stringResource(id = R.string.open_google_maps)
                )
                Text(
                    text = text,
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        },
        modifier = modifier.clickable {
            context.showMap(coordinates, label)
        }
    )
}

@DefaultPreviews
@Composable
private fun CoordinateCardPreview() {
    PreviewTheme {
        Surface {
            CoordinateListItem(
                context = LocalContext.current,
                coordinates = CoordinatesUiModel(
                    longitude = -73.98905,
                    latitude = 40.76688
                )
            )
        }
    }
}
