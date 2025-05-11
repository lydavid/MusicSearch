package ly.david.musicsearch.shared.feature.graph

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.ui.common.screen.ArtistCollaborationScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val graphFeatureModule = module {
    factory { ArtistCollaborationGraphSimulation() }
    single(named("GraphPresenter")) {
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is ArtistCollaborationScreen -> ArtistCollaborationGraphPresenter(
                    screen = screen,
                    navigator = navigator,
                    graphSimulation = get(),
                    artistCollaborationRepository = get(),
                    appPreferences = get(),
                )

                else -> null
            }
        }
    }
    single(named("GraphScreen")) {
        Ui.Factory { screen, context ->
            when (screen) {
                is ArtistCollaborationScreen -> {
                    ui<ArtistCollaborationGraphUiState> { state, modifier ->
                        ArtistCollaborationGraphUi(
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
