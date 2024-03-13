package ly.david.musicsearch.shared.feature.collections.add

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.feature.collections.components.CollectionBottomSheetContent

@Composable
internal fun AddToCollectionUi(
    state: AddToCollectionUiState,
    modifier: Modifier,
) {
    val eventSink = state.eventSink
    Column {
        CollectionBottomSheetContent(
            collections = state.lazyPagingItems,
            modifier = modifier,
            onAddToCollection = { collectionId ->
                eventSink(AddToCollectionUiEvent.AddToCollection(collectionId))
            }
        )
    }
}
