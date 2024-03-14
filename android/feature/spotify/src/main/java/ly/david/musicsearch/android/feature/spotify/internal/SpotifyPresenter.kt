package ly.david.musicsearch.android.feature.spotify.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.ui.common.screen.SearchScreen

internal class SpotifyPresenter(
    private val navigator: Navigator,
) : Presenter<SpotifyUiState> {

    @Composable
    override fun present(): SpotifyUiState {
        var metadata: SpotifyMetadata by remember { mutableStateOf(SpotifyMetadata()) }

        SpotifyBroadcastReceiver {
            metadata = it
        }

        fun eventSink(event: SpotifyUiEvent) {
            when (event) {
                is SpotifyUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is SpotifyUiEvent.GoToSearch -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            SearchScreen(
                                query = event.query,
                                entity = event.entity,
                            ),
                        ),
                    )
                }
            }
        }

        return SpotifyUiState(
            metadata = metadata,
            eventSink = ::eventSink,
        )
    }
}
