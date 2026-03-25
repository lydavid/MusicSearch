package ly.david.musicsearch.shared.feature.collections.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.collection.CreateNewCollectionResult
import ly.david.musicsearch.shared.feature.collections.components.CollectionBottomSheetContent
import ly.david.musicsearch.shared.feature.collections.create.CreateNewCollectionDialogContent
import ly.david.musicsearch.ui.common.dialog.BasicDialog

@Composable
internal fun AddToCollectionUi(
    state: AddToCollectionUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        BasicDialog(
            onDismiss = {
                showDialog = false
            },
        ) {
            CreateNewCollectionDialogContent(
                defaultEntity = state.defaultEntityType,
                onDismiss = { showDialog = false },
                onSubmit = { name, entity ->
                    eventSink(
                        AddToCollectionUiEvent.CreateNewCollection(
                            CreateNewCollectionResult.NewCollection(
                                name = name,
                                entity = entity,
                            ),
                        ),
                    )
                },
            )
        }
    }

    CollectionBottomSheetContent(
        collections = state.lazyPagingItems,
        modifier = modifier,
        onCreateCollectionClick = {
            showDialog = true
        },
        onAddToCollection = { collectionId ->
            eventSink(AddToCollectionUiEvent.AddToCollection(collectionId))
        },
    )
}
