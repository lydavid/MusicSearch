package ly.david.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ly.david.data.common.lookupInBrowser
import ly.david.data.network.MusicBrainzEntity
import ly.david.ui.common.R

@Composable
fun OverflowMenuScope.OpenInBrowserMenuItem(
    entity: MusicBrainzEntity,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    DropdownMenuItem(
        text = { Text(stringResource(id = R.string.open_in_browser)) },
        onClick = {
            context.lookupInBrowser(entity, entityId)
            closeMenu()
        },
        modifier = modifier
    )
}
