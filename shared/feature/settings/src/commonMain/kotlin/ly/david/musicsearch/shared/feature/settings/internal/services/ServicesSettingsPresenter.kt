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
import ly.david.musicsearch.shared.domain.preferences.WikidataInstance

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
        val wikidataInstance by appPreferences.wikidataInstance.collectAsState(
            initial = WikidataInstance.Default,
        )
        val hasDefaultSpotifyCredentials = appPreferences.hasDefaultSpotifyCredentials
        val artistImageSource by appPreferences.artistImageSource.collectAsState(
            initial = ArtistImageSource.Wikimedia,
        )

        fun eventSink(event: ServicesSettingsUiEvent) {
            when (event) {
                ServicesSettingsUiEvent.NavigateUp -> navigator.pop()

                is ServicesSettingsUiEvent.ConfirmMusicBrainzInstance -> {
                    appPreferences.setMusicBrainzInstance(
                        resolveInstance(
                            url = event.url,
                            isCustom = event.isCustom,
                            default = MusicBrainzInstance.Default,
                            custom = MusicBrainzInstance::Custom,
                        ),
                    )
                }

                is ServicesSettingsUiEvent.ConfirmListenBrainzInstance -> {
                    appPreferences.setListenBrainzInstance(
                        resolveInstance(
                            url = event.url,
                            isCustom = event.isCustom,
                            default = ListenBrainzInstance.Default,
                            custom = ListenBrainzInstance::Custom,
                        ),
                    )
                }

                is ServicesSettingsUiEvent.ConfirmWikimediaInstance -> {
                    appPreferences.setWikidataInstance(
                        resolveInstance(
                            url = event.url,
                            isCustom = event.isCustom,
                            default = WikidataInstance.Default,
                            custom = WikidataInstance::Custom,
                        ),
                    )
                }

                is ServicesSettingsUiEvent.ConfirmArtistImageSource -> {
                    appPreferences.setArtistImageSource(event.source)
                }

                ServicesSettingsUiEvent.Reset -> {
                    appPreferences.setMusicBrainzInstance(MusicBrainzInstance.Default)
                    appPreferences.setListenBrainzInstance(ListenBrainzInstance.Default)
                    appPreferences.setWikidataInstance(WikidataInstance.Default)
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
            wikidataInstance = wikidataInstance,
            artistImageSource = artistImageSource,
            showDefaultSpotifyOption = hasDefaultSpotifyCredentials,
            eventSink = ::eventSink,
        )
    }
}

private inline fun <T> resolveInstance(
    url: String,
    isCustom: Boolean,
    default: T,
    custom: (String) -> T,
): T {
    val cleanedUrl = url.trim().removeSuffix("/")
    return if (isCustom && cleanedUrl.isNotEmpty()) {
        custom(cleanedUrl)
    } else {
        default
    }
}

internal data class ServicesSettingsUiState(
    val musicBrainzInstance: MusicBrainzInstance,
    val listenBrainzInstance: ListenBrainzInstance,
    val wikidataInstance: WikidataInstance,
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

    data class ConfirmWikimediaInstance(
        val isCustom: Boolean,
        val url: String,
    ) : ServicesSettingsUiEvent

    data class ConfirmArtistImageSource(
        val source: ArtistImageSource,
    ) : ServicesSettingsUiEvent

    data object Reset : ServicesSettingsUiEvent
}
