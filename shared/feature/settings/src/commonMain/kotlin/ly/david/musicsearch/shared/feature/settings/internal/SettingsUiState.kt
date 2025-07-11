package ly.david.musicsearch.shared.feature.settings.internal

import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState

internal data class SettingsUiState(
    val username: String = "",
    val accessToken: String? = null,
    val showMoreInfoInReleaseListItem: Boolean = true,
    val sortReleaseGroupListItems: Boolean = false,
    val showCrashReporterSettings: Boolean = false,
    val isCrashReportingEnabled: Boolean = false,
    val loginState: LoginUiState = LoginUiState(),
    val snackbarMessage: String? = null,
    val appDatabaseVersion: String = "",
    val sqliteVersion: String = "",
    val isDeveloperMode: Boolean = false,
    val eventSink: (SettingsUiEvent) -> Unit = {},
) : CircuitUiState
