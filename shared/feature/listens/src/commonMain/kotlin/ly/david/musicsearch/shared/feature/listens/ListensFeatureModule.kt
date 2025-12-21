package ly.david.musicsearch.shared.feature.listens

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.ui.common.screen.ListensScreen
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
                            modifier = modifier,
                        )
                    }
                }

                else -> null
            }
        }
    }
}
