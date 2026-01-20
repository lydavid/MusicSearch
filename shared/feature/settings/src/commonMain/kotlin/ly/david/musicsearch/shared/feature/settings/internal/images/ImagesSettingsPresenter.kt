package ly.david.musicsearch.shared.feature.settings.internal.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.DEFAULT_IMAGES_GRID_PADDING_DP
import ly.david.musicsearch.shared.domain.DEFAULT_NUMBER_OF_IMAGES_PER_ROW
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.spotify.IsUserSpotifyAuthSet
import ly.david.musicsearch.shared.domain.spotify.SetUserSpotifyAuth

internal class ImagesSettingsPresenter(
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val setUserSpotifyAuth: SetUserSpotifyAuth,
    private val isUserSpotifyAuthSet: IsUserSpotifyAuthSet
) : Presenter<ImagesSettingsUiState> {
    @Composable
    override fun present(): ImagesSettingsUiState {
        val numberOfImagesPerRow by appPreferences.observeNumberOfImagesPerRow.collectAsState(
            initial = DEFAULT_NUMBER_OF_IMAGES_PER_ROW,
        )
        val imagesGridPaddingDp by appPreferences.observeImagesGridPaddingDp.collectAsState(
            initial = DEFAULT_IMAGES_GRID_PADDING_DP,
        )
        val isUserSpotifyAuthSet by isUserSpotifyAuthSet().collectAsState(
            initial = false,
        )

        val coroutineScope = rememberCoroutineScope()

        fun eventSink(event: ImagesSettingsUiEvent) {
            when (event) {
                ImagesSettingsUiEvent.NavigateUp -> navigator.pop()

                is ImagesSettingsUiEvent.UpdateNumberOfImagesPerRow -> {
                    appPreferences.setNumberOfImagesPerRow(event.numberOfImagesPerRow)
                }

                is ImagesSettingsUiEvent.UpdateImagesGridPaddingDp -> {
                    appPreferences.setImagesGridPaddingDp(event.imagesGridPaddingDp)
                }

                is ImagesSettingsUiEvent.UpdateUserSpotifyAuth -> {
                    coroutineScope.launch {
                        setUserSpotifyAuth(
                            clientId = event.clientId,
                            clientSecret = event.clientSecret,
                        )
                    }
                }
            }
        }

        return ImagesSettingsUiState(
            numberOfImagesPerRow = numberOfImagesPerRow,
            imagesGridPaddingDp = imagesGridPaddingDp,
            isUserSpotifyAuthSet = isUserSpotifyAuthSet,
            eventSink = ::eventSink,
        )
    }
}

internal data class ImagesSettingsUiState(
    val numberOfImagesPerRow: Int = DEFAULT_NUMBER_OF_IMAGES_PER_ROW,
    val imagesGridPaddingDp: Int = DEFAULT_IMAGES_GRID_PADDING_DP,
    val isUserSpotifyAuthSet: Boolean = false,
    val eventSink: (ImagesSettingsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface ImagesSettingsUiEvent : CircuitUiEvent {
    data object NavigateUp : ImagesSettingsUiEvent
    data class UpdateNumberOfImagesPerRow(val numberOfImagesPerRow: Int) : ImagesSettingsUiEvent
    data class UpdateImagesGridPaddingDp(val imagesGridPaddingDp: Int) : ImagesSettingsUiEvent
    data class UpdateUserSpotifyAuth(
        val clientId: String,
        val clientSecret: String,
    ) : ImagesSettingsUiEvent
}
