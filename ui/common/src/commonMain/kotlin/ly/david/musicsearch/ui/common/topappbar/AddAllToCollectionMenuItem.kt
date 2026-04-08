package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.PlaylistAdd
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.addXCountToCollection
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverflowMenuScope.AddAllToCollectionMenuItem(
    tab: Tab?,
    entityIds: ImmutableList<String>,
    overlayHost: OverlayHost,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val entity = tab?.toMusicBrainzEntityTypeWhereTracksAreRecordings() ?: return
    if (entityIds.isEmpty()) return

    // prefer a distinct list over a set to preserve order
    val distinctEntityIds = entityIds.distinct()

    DropdownMenuItem(
        text = {
            Text(
                text = stringResource(Res.string.addXCountToCollection, distinctEntityIds.size),
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
                entityType = entity,
                entityIds = distinctEntityIds,
                snackbarHostState = snackbarHostState,
                onLoginClick = onLoginClick,
            )
            closeMenu()
        },
        modifier = modifier,
    )
}
