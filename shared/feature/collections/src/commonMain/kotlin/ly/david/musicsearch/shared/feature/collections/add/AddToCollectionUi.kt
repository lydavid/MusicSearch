package ly.david.musicsearch.shared.feature.collections.add

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import ly.david.musicsearch.shared.feature.collections.components.CollectionBottomSheetContent
import ly.david.musicsearch.shared.feature.collections.components.CreateCollectionDialogContent
import ly.david.musicsearch.shared.feature.collections.list.NewCollection

@Composable
internal fun AddToCollectionUi(
    state: AddToCollectionUiState,
    modifier: Modifier,
) {
    val eventSink = state.eventSink
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
            },
        ) {
            Surface {
                CreateCollectionDialogContent(
                    onDismiss = { showDialog = false },
                    onSubmit = { name, entity ->
                        eventSink(
                            AddToCollectionUiEvent.CreateCollection(
                                NewCollection(
                                    name,
                                    entity,
                                ),
                            ),
                        )
                    },
                )
            }
        }
    }

    Column(modifier = modifier) {
        CollectionBottomSheetContent(
            collections = state.lazyPagingItems,
            onCreateCollectionClick = {
                showDialog = true
            },
            onAddToCollection = { collectionId ->
                eventSink(AddToCollectionUiEvent.AddToCollection(collectionId))
            },
        )
    }
}
