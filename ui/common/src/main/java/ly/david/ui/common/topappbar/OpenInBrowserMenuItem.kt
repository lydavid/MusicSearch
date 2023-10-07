package ly.david.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ly.david.data.common.lookupInBrowser
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.strings.LocalStrings

@Composable
fun OverflowMenuScope.OpenInBrowserMenuItem(
    entity: MusicBrainzEntity,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val context = LocalContext.current

    DropdownMenuItem(
        text = { Text(strings.openInBrowser) },
        onClick = {
            context.lookupInBrowser(entity, entityId)
            closeMenu()
        },
        modifier = modifier
    )
}
