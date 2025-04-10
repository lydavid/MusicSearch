package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.ExportDatabase
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.auth.usecase.Logout
import ly.david.musicsearch.shared.domain.metadata.MetadataRepository
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter

internal class SettingsPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val loginPresenter: LoginPresenter,
    private val logout: Logout,
    private val exportDatabase: ExportDatabase,
    private val metadataRepository: MetadataRepository,
) : Presenter<SettingsUiState> {
    @Composable
    override fun present(): SettingsUiState {
        val username by musicBrainzAuthStore.username.collectAsState(initial = "")
        val accessToken by musicBrainzAuthStore.accessToken.collectAsState(initial = null)
        val theme by appPreferences.theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
        val useMaterialYou by appPreferences.useMaterialYou.collectAsState(initial = true)
        val sortReleaseGroupListItems by appPreferences.sortReleaseGroupListItems.collectAsState(initial = true)
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsState(initial = true)
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }

        val scope = rememberCoroutineScope()
        val loginUiState = loginPresenter.present()

        fun eventSink(event: SettingsUiEvent) {
            when (event) {
                is SettingsUiEvent.UpdateTheme -> {
                    appPreferences.setTheme(event.theme)
                }

                is SettingsUiEvent.UpdateUseMaterialYou -> {
                    appPreferences.setUseMaterialYou(event.use)
                }

                is SettingsUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.show)
                }

                is SettingsUiEvent.UpdateSortReleaseGroupListItems -> {
                    appPreferences.setSortReleaseGroupListItems(event.sort)
                }

                is SettingsUiEvent.GoToScreen -> {
                    navigator.goTo(event.screen)
                }

                is SettingsUiEvent.Logout -> {
                    scope.launch {
                        logout()
                    }
                }

                is SettingsUiEvent.ExportDatabase -> {
                    snackbarMessage = exportDatabase()
                }
            }
        }

        return SettingsUiState(
            username = username,
            accessToken = accessToken,
            theme = theme,
            useMaterialYou = useMaterialYou,
            sortReleaseGroupListItems = sortReleaseGroupListItems,
            showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
            loginState = loginUiState,
            snackbarMessage = snackbarMessage,
            databaseVersion = metadataRepository.getDatabaseVersion(),
            eventSink = ::eventSink,
        )
    }
}
