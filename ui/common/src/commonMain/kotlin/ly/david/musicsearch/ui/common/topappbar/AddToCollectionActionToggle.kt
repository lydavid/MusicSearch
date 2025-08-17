package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.icon.AddToCollectionIcon

@Composable
fun AddToCollectionActionToggle(
    collected: Boolean,
    entity: MusicBrainzEntityType,
    entityId: String,
    overlayHost: OverlayHost,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    nameWithDisambiguation: String = "",
) {
    // TODO: support removing current entity from collections from bottom sheet
    IconButton(
        modifier = modifier,
        onClick = {
            showAddToCollectionSheet(
                coroutineScope = coroutineScope,
                overlayHost = overlayHost,
                entity = entity,
                entityIds = setOf(entityId),
                snackbarHostState = snackbarHostState,
                onLoginClick = onLoginClick,
            )
        },
    ) {
        AddToCollectionIcon(
            collected = collected,
            nameWithDisambiguation = nameWithDisambiguation,
        )
    }
}
