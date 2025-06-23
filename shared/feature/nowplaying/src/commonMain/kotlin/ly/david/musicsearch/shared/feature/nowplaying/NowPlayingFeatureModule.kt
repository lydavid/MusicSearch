package ly.david.musicsearch.shared.feature.nowplaying

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.ui.common.screen.NowPlayingHistoryScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val nowPlayingFeatureModule = module {
    single(named("NowPlayingHistoryScreen")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is NowPlayingHistoryScreen -> NowPlayingHistoryPresenter(
                    navigator = navigator,
                    getNowPlayingHistory = get(),
                    deleteNowPlayingHistory = get(),
                )

                else -> null
            }
        }
    }
    single(named("NowPlayingHistoryScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is NowPlayingHistoryScreen -> {
                    ui<NowPlayingHistoryUiState> { state, modifier ->
                        NowPlayingHistoryUi(
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
