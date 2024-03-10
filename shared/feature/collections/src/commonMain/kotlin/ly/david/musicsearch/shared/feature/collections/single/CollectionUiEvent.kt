package ly.david.musicsearch.shared.feature.collections.single

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface CollectionUiEvent : CircuitUiEvent {
    data object NavigateUp : CollectionUiEvent

    data class UpdateQuery(val query: String) : CollectionUiEvent

    data class UpdateSortReleaseGroupListItems(val sort: Boolean) : CollectionUiEvent

    data class MarkItemForDeletion(
        val collectableId: String,
        val name: String,
    ) : CollectionUiEvent

    data class UnMarkItemForDeletion(val collectableId: String) : CollectionUiEvent

    data class RequestForMissingCoverArtUrl(
        val entityId: String,
        val entity: MusicBrainzEntity,
    ) : CollectionUiEvent

    data class DeleteItem(
        val collectableId: String,
        val name: String,
    ) : CollectionUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : CollectionUiEvent
}
