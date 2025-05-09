package ly.david.musicsearch.shared.feature.settings.internal

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.screen.Screen

internal sealed interface SettingsUiEvent : CircuitUiEvent {
    data class UpdateShowMoreInfoInReleaseListItem(val show: Boolean) : SettingsUiEvent
    data class UpdateSortReleaseGroupListItems(val sort: Boolean) : SettingsUiEvent
    data class EnableCrashReporting(val enable: Boolean) : SettingsUiEvent
    data class GoToScreen(val screen: Screen) : SettingsUiEvent
    data object Logout : SettingsUiEvent
    data object ExportDatabase : SettingsUiEvent
}
