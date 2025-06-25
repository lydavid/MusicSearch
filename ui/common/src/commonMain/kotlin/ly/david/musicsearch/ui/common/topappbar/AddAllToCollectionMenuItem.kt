package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.PlaylistAdd
import ly.david.musicsearch.ui.common.screen.AddToCollectionScreen
import ly.david.musicsearch.ui.common.screen.showInBottomSheet

@Composable
fun OverflowMenuScope.AddAllToCollectionMenuItem(
    tab: Tab,
    entityIds: Set<String>,
    overlayHost: OverlayHost,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val entity = tab.toMusicBrainzEntity() ?: return
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
            coroutineScope.launch {
                val result = overlayHost.showInBottomSheet(
                    AddToCollectionScreen(
                        entity = entity,
                        collectableIds = entityIds,
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
            closeMenu()
        },
        modifier = modifier,
    )
}
