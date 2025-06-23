package ly.david.musicsearch.shared.feature.spotify

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.spotify.history.SpotifyHistoryPresenter
import ly.david.musicsearch.shared.feature.spotify.history.SpotifyHistoryUi
import ly.david.musicsearch.shared.feature.spotify.history.SpotifyUiState
import ly.david.musicsearch.ui.common.screen.SpotifyHistoryScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val spotifyFeatureModule = module {
    single(named("SpotifyHistoryScreen")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is SpotifyHistoryScreen -> SpotifyHistoryPresenter(
                    navigator = navigator,
                    spotifyHistoryRepository = get(),
                )

                else -> null
            }
        }
    }
    single(named("SpotifyHistoryScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is SpotifyHistoryScreen -> {
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
