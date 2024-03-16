package ly.david.musicsearch.shared.feature.collections.add

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.shared.feature.collections.list.NewCollection

internal sealed interface AddToCollectionUiEvent : CircuitUiEvent {
    data class CreateCollection(val newCollection: NewCollection) : AddToCollectionUiEvent
    data class AddToCollection(
        val id: String,
    ) : AddToCollectionUiEvent
}
