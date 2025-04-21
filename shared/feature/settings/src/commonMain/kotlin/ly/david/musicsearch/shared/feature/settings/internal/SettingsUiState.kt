package ly.david.musicsearch.shared.feature.settings.internal

import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState

internal data class SettingsUiState(
    val username: String,
    val accessToken: String?,
    val theme: AppPreferences.Theme,
    val useMaterialYou: Boolean,
    val showMoreInfoInReleaseListItem: Boolean = true,
    val sortReleaseGroupListItems: Boolean = false,
    val showCrashReporterSettings: Boolean = false,
    val isCrashReportingEnabled: Boolean = false,
    val loginState: LoginUiState,
    val snackbarMessage: String? = null,
    val databaseVersion: String,
    val eventSink: (SettingsUiEvent) -> Unit,
) : CircuitUiState
