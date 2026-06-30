package ly.david.musicsearch.shared.feature.settings.internal.services

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.image.ArtistImageSource
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.preferences.ListenBrainzInstance
import ly.david.musicsearch.shared.domain.preferences.MusicBrainzInstance

internal class ServicesSettingsPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
) : Presenter<ServicesSettingsUiState> {
    @Composable
    override fun present(): ServicesSettingsUiState {
        val musicBrainzInstance by appPreferences.musicBrainzInstance.collectAsState(
            initial = MusicBrainzInstance.Default,
        )
        val listenBrainzInstance by appPreferences.listenBrainzInstance.collectAsState(
            initial = ListenBrainzInstance.Default,
        )
        val hasDefaultSpotifyCredentials = appPreferences.hasDefaultSpotifyCredentials
        val artistImageSource by appPreferences.artistImageSource.collectAsState(
            initial = ArtistImageSource.Wikimedia,
        )

        fun String.cleanedUrl(): String {
            return this.trim().removeSuffix("/")
        }

        fun eventSink(event: ServicesSettingsUiEvent) {
            when (event) {
                ServicesSettingsUiEvent.NavigateUp -> navigator.pop()

                is ServicesSettingsUiEvent.ConfirmMusicBrainzInstance -> {
                    val cleanedUrl = event.url.cleanedUrl()
                    val instance = if (event.isCustom && cleanedUrl.isNotEmpty()) {
                        MusicBrainzInstance.Custom(url = cleanedUrl)
                    } else {
                        MusicBrainzInstance.Default
                    }
                    appPreferences.setMusicBrainzInstance(instance)
                }

                is ServicesSettingsUiEvent.ConfirmListenBrainzInstance -> {
                    val cleanedUrl = event.url.cleanedUrl()
                    val instance = if (event.isCustom && cleanedUrl.isNotEmpty()) {
                        ListenBrainzInstance.Custom(url = cleanedUrl)
                    } else {
                        ListenBrainzInstance.Default
                    }
                    appPreferences.setListenBrainzInstance(instance)
                }

                is ServicesSettingsUiEvent.ConfirmArtistImageSource -> {
                    appPreferences.setArtistImageSource(event.source)
                }

                ServicesSettingsUiEvent.Reset -> {
                    appPreferences.setMusicBrainzInstance(MusicBrainzInstance.Default)
                    appPreferences.setListenBrainzInstance(ListenBrainzInstance.Default)
                    appPreferences.setArtistImageSource(
                        if (hasDefaultSpotifyCredentials) {
                            ArtistImageSource.Spotify.Default
                        } else {
                            ArtistImageSource.Wikimedia
                        },
                    )
                }
            }
        }

        return ServicesSettingsUiState(
            musicBrainzInstance = musicBrainzInstance,
            listenBrainzInstance = listenBrainzInstance,
            artistImageSource = artistImageSource,
            showDefaultSpotifyOption = hasDefaultSpotifyCredentials,
            eventSink = ::eventSink,
        )
    }
}

internal data class ServicesSettingsUiState(
    val musicBrainzInstance: MusicBrainzInstance,
    val listenBrainzInstance: ListenBrainzInstance,
    val artistImageSource: ArtistImageSource,
    val showDefaultSpotifyOption: Boolean,
    val eventSink: (ServicesSettingsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface ServicesSettingsUiEvent : CircuitUiEvent {
    data object NavigateUp : ServicesSettingsUiEvent

    data class ConfirmMusicBrainzInstance(
        val isCustom: Boolean,
        val url: String,
    ) : ServicesSettingsUiEvent

    data class ConfirmListenBrainzInstance(
        val isCustom: Boolean,
        val url: String,
    ) : ServicesSettingsUiEvent

    data class ConfirmArtistImageSource(
        val source: ArtistImageSource,
    ) : ServicesSettingsUiEvent

    data object Reset : ServicesSettingsUiEvent
}
