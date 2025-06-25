package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.icons.Bookmark
import ly.david.musicsearch.ui.common.icons.BookmarkFilled
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.screen.AddToCollectionScreen
import ly.david.musicsearch.ui.common.screen.showInBottomSheet

@Composable
fun AddToCollectionActionToggle(
    partOfACollection: Boolean,
    entity: MusicBrainzEntity,
    entityId: String,
    overlayHost: OverlayHost,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: support removing current entity from collections from bottom sheet
    IconButton(
        modifier = modifier,
        onClick = {
            coroutineScope.launch {
                val result = overlayHost.showInBottomSheet(
                    AddToCollectionScreen(
                        entity = entity,
                        collectableIds = setOf(entityId),
                    ),
                )
                result.message.ifNotNullOrEmpty {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = result.message,
                        actionLabel = result.actionLabel,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true,
                    )

                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> {
                            onLoginClick()
                        }

                        SnackbarResult.Dismissed -> {
                            // Do nothing.
                        }
                    }
                }
            }
        },
    ) {
        Icon(
            imageVector = if (partOfACollection) {
                CustomIcons.BookmarkFilled
            } else {
                CustomIcons.Bookmark
            },
            tint = if (partOfACollection) {
                MaterialTheme.colorScheme.primary
            } else {
                LocalContentColor.current
            },
            contentDescription = if (partOfACollection) {
                "Add to another collection"
            } else {
                "Add to collection"
            },
        )
    }
}
