package ly.david.musicsearch.ui.common.place

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import ly.david.musicsearch.shared.domain.place.formatForDisplay
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.PinDrop
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.text.TextWithHeading
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.coordinates
import musicsearch.ui.common.generated.resources.openGoogleMaps
import org.jetbrains.compose.resources.stringResource

private const val ZOOM_LEVEL = 16

/**
 * Similar to [TextWithHeading] but because it's clickable, there's no selection, but instead long click to copy.
 */
@Composable
fun CoordinateListItem(
    coordinates: CoordinatesUiModel,
    modifier: Modifier = Modifier,
    label: String? = null,
    filterText: String = "",
) {
    val formattedCoordinates = coordinates.formatForDisplay() ?: return
    val uriHandler = LocalUriHandler.current
    val coroutineScope = rememberCoroutineScope()
    val clipboard = LocalClipboard.current

    val fullAnnotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("${stringResource(Res.string.coordinates)}: ")
        }
        append(formattedCoordinates)
    }

    if (fullAnnotatedText.text.contains(filterText, ignoreCase = true)) {
        Box(
            modifier = modifier
                .combinedClickable(
                    onClick = {
                        // TODO: need different implementation for desktop
                        val latitude = coordinates.latitude ?: return@combinedClickable
                        val longitude = coordinates.longitude ?: return@combinedClickable
                        val uri = if (label.isNullOrEmpty()) {
                            "geo:$latitude,$longitude?z=$ZOOM_LEVEL"
                        } else {
                            "geo:0,0?q=$latitude,$longitude($label)&z=$ZOOM_LEVEL"
                        }
                        uriHandler.openUri(uri)
                    },
                    onLongClick = {
                        coroutineScope.launch {
                            clipboard.setClipEntry(clipEntryWith(formattedCoordinates))
                        }
                    },
                )
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 4.dp,
                ),
        ) {
            HighlightableText(
                text = fullAnnotatedText,
                highlightedText = filterText,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 24.dp),
            )

            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                imageVector = CustomIcons.PinDrop,
                contentDescription = stringResource(Res.string.openGoogleMaps),
            )
        }
    }
}
