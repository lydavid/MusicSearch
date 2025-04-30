package ly.david.musicsearch.ui.common.place

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import ly.david.musicsearch.shared.domain.place.formatForDisplay
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.PinDrop
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

private const val ZOOM_LEVEL = 16

@Composable
fun CoordinateListItem(
    coordinates: CoordinatesUiModel,
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    val strings = LocalStrings.current
    val uriHandler = LocalUriHandler.current
    val text = coordinates.formatForDisplay() ?: return

    ListItem(
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    imageVector = CustomIcons.PinDrop,
                    contentDescription = strings.openGoogleMaps,
                )
                Text(
                    text = text,
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        },
        modifier = modifier.clickable {
            // TODO: need different implementation for desktop
            //  come back once we've decided on a view modeler
            val latitude = coordinates.latitude ?: return@clickable
            val longitude = coordinates.longitude ?: return@clickable
            val uri = if (label.isNullOrEmpty()) {
                "geo:$latitude,$longitude?z=$ZOOM_LEVEL"
            } else {
                "geo:0,0?q=$latitude,$longitude($label)&z=$ZOOM_LEVEL"
            }
            uriHandler.openUri(uri)
        },
    )
}
