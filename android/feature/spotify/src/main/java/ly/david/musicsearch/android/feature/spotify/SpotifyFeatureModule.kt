package ly.david.musicsearch.android.feature.spotify

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.android.feature.spotify.internal.SpotifyPresenter
import ly.david.musicsearch.android.feature.spotify.internal.SpotifyUi
import ly.david.musicsearch.android.feature.spotify.internal.SpotifyUiState
import ly.david.ui.common.screen.SpotifyPlayingScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val spotifyFeatureModule = module {
    single(named(SpotifyPlayingScreen::class.java.name)) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is SpotifyPlayingScreen -> SpotifyPresenter(
                    navigator = navigator,
                )

                else -> null
            }
        }
    }
    single(named(SpotifyPlayingScreen::class.java.name)) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is SpotifyPlayingScreen -> {
                    ui<SpotifyUiState> { state, modifier ->
                        SpotifyUi(
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
