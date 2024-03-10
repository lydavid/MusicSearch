package ly.david.musicsearch.shared.feature.details.area

import com.slack.circuit.runtime.CircuitUiEvent

internal sealed interface AreaUiEvent : CircuitUiEvent {
    data object NavigateUp : AreaUiEvent
    data class UpdateQuery(val query: String) : AreaUiEvent
    data class UpdateShowMoreInfoInReleaseListItem(val showMore: Boolean) : AreaUiEvent
}
