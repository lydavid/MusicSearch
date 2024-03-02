package ly.david.musicsearch.shared.feature.collections.list

import com.slack.circuit.runtime.CircuitUiEvent

internal sealed interface CollectionListUiEvent : CircuitUiEvent {
    data class UpdateQuery(val query: String) : CollectionListUiEvent
    data class UpdateShowLocal(val show: Boolean) : CollectionListUiEvent
    data class UpdateShowRemote(val show: Boolean) : CollectionListUiEvent
    data class CreateCollection(val newCollection: NewCollection) : CollectionListUiEvent
    data class ClickCollection(
        val id: String,
    ) : CollectionListUiEvent
}
