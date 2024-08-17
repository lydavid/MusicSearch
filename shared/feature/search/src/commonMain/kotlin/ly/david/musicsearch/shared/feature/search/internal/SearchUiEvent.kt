package ly.david.musicsearch.shared.feature.search.internal

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.shared.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

internal sealed interface SearchUiEvent : CircuitUiEvent {
    data class UpdateEntity(val entity: MusicBrainzEntity) : SearchUiEvent
    data class UpdateQuery(val query: String) : SearchUiEvent
    data object RecordSearch : SearchUiEvent
    data class DeleteSearchHistory(val item: SearchHistoryListItemModel) : SearchUiEvent
    data object DeleteAllEntitySearchHistory : SearchUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : SearchUiEvent
}
