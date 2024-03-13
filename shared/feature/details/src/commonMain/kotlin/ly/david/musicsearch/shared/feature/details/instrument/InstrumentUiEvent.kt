package ly.david.musicsearch.shared.feature.details.instrument

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface InstrumentUiEvent : CircuitUiEvent {
    data object NavigateUp : InstrumentUiEvent
    data object ForceRefresh : InstrumentUiEvent
    data class UpdateTab(val tab: InstrumentTab) : InstrumentUiEvent
    data class UpdateQuery(val query: String) : InstrumentUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : InstrumentUiEvent
}
