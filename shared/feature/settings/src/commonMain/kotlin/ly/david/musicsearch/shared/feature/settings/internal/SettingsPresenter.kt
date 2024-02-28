package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.auth.MusicBrainzAuthStore
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.data.musicbrainz.auth.Logout

internal class SettingsPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val loginPresenter: LoginPresenter,
    private val logout: Logout,
) : Presenter<SettingsUiState> {
    @Composable
    override fun present(): SettingsUiState {
        val username by musicBrainzAuthStore.username.collectAsState(initial = "")
        val accessToken by musicBrainzAuthStore.accessToken.collectAsState(initial = null)
        val theme by appPreferences.theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
        val useMaterialYou by appPreferences.useMaterialYou.collectAsState(initial = true)
        val sortReleaseGroupListItems by appPreferences.sortReleaseGroupListItems.collectAsState(initial = true)
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsState(initial = true)

        val scope = rememberCoroutineScope()
        val loginUiState = loginPresenter.present()
        val loginEventSink = loginUiState.eventSink

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

                is SettingsUiEvent.Login -> {
                    loginEventSink(LoginUiEvent.StartLogin)
                }

                is SettingsUiEvent.Logout -> {
                    scope.launch {
                        logout()
                    }
                }

                is SettingsUiEvent.DismissDialog -> {
                    loginEventSink(LoginUiEvent.DismissDialog)
                }

                is SettingsUiEvent.SubmitAuthCode -> {
                    loginEventSink(LoginUiEvent.SubmitAuthCode(event.authCode))
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
            showDialog = loginUiState.showDialog,
            eventSink = ::eventSink,
        )
    }
}
