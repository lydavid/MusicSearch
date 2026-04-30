package ly.david.musicsearch.shared.feature.listens

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.datetime.TimeZone
import ly.david.musicsearch.shared.feature.listens.submit.SubmitListenPresenter
import ly.david.musicsearch.shared.feature.listens.submit.SubmitListenUi
import ly.david.musicsearch.shared.feature.listens.submit.SubmitListenUiState
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.screen.SubmitListenScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val listensFeatureModule = module {
    single(named("ListensPresenter")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is ListensScreen -> ListensPresenter(
                    screen = screen,
                    navigator = navigator,
                    listenBrainzAuthStore = get(),
                    listensListRepository = get(),
                    listenBrainzRepository = get(),
                    externalScope = get(),
                    appPreferences = get(),
                )

                is SubmitListenScreen -> SubmitListenPresenter(
                    screen = screen,
                    navigator = navigator,
                    listenBrainzAuthStore = get(),
                    listensListRepository = get(),
                    getTracksByReleaseForListenSubmission = get(),
                    clock = get(),
                    timeZone = TimeZone.currentSystemDefault(),
                )

                else -> null
            }
        }
    }
    single(named("ListensScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is ListensScreen -> {
                    ui<ListensUiState> { state, modifier ->
                        ListensUi(
                            state = state,
                            timeZone = TimeZone.currentSystemDefault(),
                            clock = get(),
                            modifier = modifier,
                        )
                    }
                }

                is SubmitListenScreen -> {
                    ui<SubmitListenUiState> { state, modifier ->
                        SubmitListenUi(
                            state = state,
                            timeZone = TimeZone.currentSystemDefault(),
                            clock = get(),
                            modifier = modifier,
                        )
                    }
                }

                else -> null
            }
        }
    }
}
