package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.PlaylistAdd

@Composable
fun OverflowMenuScope.AddAllToCollectionMenuItem(
    tab: Tab?,
    entityIds: Set<String>,
    overlayHost: OverlayHost,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val entity = tab?.toMusicBrainzEntity() ?: return
    if (entityIds.isEmpty()) return

    DropdownMenuItem(
        text = {
            Text(
                text = "Add ${entityIds.size} to collection",
            )
        },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.PlaylistAdd,
                contentDescription = null,
            )
        },
        onClick = {
            showAddToCollectionSheet(
                coroutineScope = coroutineScope,
                overlayHost = overlayHost,
                entity = entity,
                entityIds = entityIds,
                snackbarHostState = snackbarHostState,
                onLoginClick = onLoginClick,
            )
            closeMenu()
        },
        modifier = modifier,
    )
}
