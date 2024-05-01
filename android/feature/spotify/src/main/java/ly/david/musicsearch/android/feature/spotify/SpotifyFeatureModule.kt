package ly.david.musicsearch.android.feature.spotify

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.android.feature.spotify.history.SpotifyHistoryUi
import ly.david.ui.common.screen.SpotifyPlayingScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val spotifyFeatureModule = module {
    single(named("SpotifyPlayingScreen")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is SpotifyPlayingScreen -> SpotifyHistoryPresenter(
                    navigator = navigator,
                    spotifyHistoryRepository = get(),
                )

                else -> null
            }
        }
    }
    single(named("SpotifyPlayingScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is SpotifyPlayingScreen -> {
                    ui<SpotifyUiState> { state, modifier ->
                        SpotifyHistoryUi(
                            state = state,
                            modifier = modifier,
                        )
                    }
                }

                else -> null
            }
        }
    }
}
