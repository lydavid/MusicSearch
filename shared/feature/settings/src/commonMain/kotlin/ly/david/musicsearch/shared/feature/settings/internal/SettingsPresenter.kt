package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.ExportDatabase
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.auth.usecase.MusicBrainzLogout
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzLoginState
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import ly.david.musicsearch.shared.domain.listen.UpdateListenBrainzToken
import ly.david.musicsearch.shared.domain.metadata.MetadataRepository
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiState

internal class SettingsPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzLoginPresenter: MusicBrainzLoginPresenter,
    private val musicBrainzLogout: MusicBrainzLogout,
    private val listenBrainzRepository: ListenBrainzRepository,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val updateListenBrainzToken: UpdateListenBrainzToken,
    private val exportDatabase: ExportDatabase,
    private val metadataRepository: MetadataRepository,
) : Presenter<SettingsUiState> {
    @Composable
    override fun present(): SettingsUiState {
        val musicBrainzUsername by musicBrainzAuthStore.username.collectAsRetainedState("")
        val musicBrainzAccessToken by musicBrainzAuthStore.accessToken.collectAsRetainedState(null)

        var textFieldText by rememberSaveable { mutableStateOf("") }
        var listenBrainzLoginState: ListenBrainzLoginState? by rememberRetained { mutableStateOf(null) }
        val listenBrainzUsername by listenBrainzAuthStore.username.collectAsRetainedState("")
        val listenBrainzUserToken by listenBrainzAuthStore.observeUserToken().collectAsRetainedState("")

        val sortReleaseListItems by appPreferences.sortReleaseListItems.collectAsRetainedState(false)
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsRetainedState(true)
        val sortReleaseGroupListItems by appPreferences.sortReleaseGroupListItems.collectAsRetainedState(false)
        val showCrashReporterSettings = appPreferences.showCrashReporterSettings
        val isCrashReportingEnabled by appPreferences.isCrashReportingEnabled.collectAsRetainedState(false)
        val isDeveloperMode by appPreferences.isDeveloperMode.collectAsRetainedState(false)
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }

        val coroutineScope = rememberCoroutineScope()
        val loginUiState = musicBrainzLoginPresenter.present()

        fun eventSink(event: SettingsUiEvent) {
            when (event) {
                is SettingsUiEvent.UpdateSortReleaseListItems -> {
                    appPreferences.setSortReleaseListItems(event.sort)
                }

                is SettingsUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.show)
                }

                is SettingsUiEvent.UpdateSortReleaseGroupListItems -> {
                    appPreferences.setSortReleaseGroupListItems(event.sort)
                }

                is SettingsUiEvent.EnableCrashReporting -> {
                    appPreferences.setEnableCrashReporting(event.enable)
                }

                is SettingsUiEvent.GoToScreen -> {
                    navigator.goTo(event.screen)
                }

                is SettingsUiEvent.MusicBrainzLogout -> {
                    coroutineScope.launch {
                        musicBrainzLogout()
                    }
                }

                is SettingsUiEvent.UpdateListenBrainzUserToken -> {
                    textFieldText = event.token
                }

                is SettingsUiEvent.ListenBrainzLogin -> {
                    coroutineScope.launch {
                        listenBrainzLoginState = updateListenBrainzToken(token = textFieldText)
                    }
                }

                is SettingsUiEvent.ListenBrainzLogout -> {
                    coroutineScope.launch {
                        listenBrainzLoginState = updateListenBrainzToken(token = "")
                    }
                }

                is SettingsUiEvent.DismissSnackbar -> {
                    snackbarMessage = null
                    listenBrainzLoginState = null
                }

                is SettingsUiEvent.ExportDatabase -> {
                    coroutineScope.launch {
                        snackbarMessage = exportDatabase()
                    }
                }

                is SettingsUiEvent.EnableDeveloperMode -> {
                    appPreferences.setDeveloperMode(event.enable)
                }
            }
        }

        return SettingsUiState(
            musicBrainzUsername = musicBrainzUsername,
            musicBrainzAccessToken = musicBrainzAccessToken,
            listenBrainzUrl = listenBrainzRepository.getBaseUrl(),
            listenBrainzText = textFieldText,
            listenBrainzLoginState = listenBrainzLoginState,
            listenBrainzUsername = listenBrainzUsername,
            listenBrainzUserToken = listenBrainzUserToken,
            showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
            sortReleaseListItems = sortReleaseListItems,
            sortReleaseGroupListItems = sortReleaseGroupListItems,
            showCrashReporterSettings = showCrashReporterSettings,
            isCrashReportingEnabled = isCrashReportingEnabled,
            musicBrainzLoginUiState = loginUiState,
            snackbarMessage = snackbarMessage,
            appDatabaseVersion = metadataRepository.getAppDatabaseVersion(),
            sqliteVersion = metadataRepository.getSQLiteVersion(),
            isDeveloperMode = isDeveloperMode,
            eventSink = ::eventSink,
        )
    }
}

internal data class SettingsUiState(
    val musicBrainzUsername: String = "",
    val musicBrainzAccessToken: String? = null,
    val listenBrainzUrl: String = "",
    val listenBrainzText: String = "",
    val listenBrainzLoginState: ListenBrainzLoginState? = null,
    val listenBrainzUsername: String = "",
    val listenBrainzUserToken: String = "",
    val sortReleaseListItems: Boolean = false,
    val showMoreInfoInReleaseListItem: Boolean = true,
    val sortReleaseGroupListItems: Boolean = false,
    val showCrashReporterSettings: Boolean = false,
    val isCrashReportingEnabled: Boolean = false,
    val musicBrainzLoginUiState: MusicBrainzLoginUiState = MusicBrainzLoginUiState(),
    val snackbarMessage: String? = null,
    val appDatabaseVersion: String = "",
    val sqliteVersion: String = "",
    val isDeveloperMode: Boolean = false,
    val eventSink: (SettingsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface SettingsUiEvent : CircuitUiEvent {
    data object MusicBrainzLogout : SettingsUiEvent

    data class UpdateListenBrainzUserToken(val token: String) : SettingsUiEvent
    data object ListenBrainzLogin : SettingsUiEvent
    data object ListenBrainzLogout : SettingsUiEvent

    data object DismissSnackbar : SettingsUiEvent
    data class UpdateSortReleaseListItems(val sort: Boolean) : SettingsUiEvent
    data class UpdateShowMoreInfoInReleaseListItem(val show: Boolean) : SettingsUiEvent
    data class UpdateSortReleaseGroupListItems(val sort: Boolean) : SettingsUiEvent
    data class EnableCrashReporting(val enable: Boolean) : SettingsUiEvent
    data class GoToScreen(val screen: Screen) : SettingsUiEvent
    data object ExportDatabase : SettingsUiEvent
    data class EnableDeveloperMode(val enable: Boolean) : SettingsUiEvent
}
