package ly.david.musicsearch.shared.feature.graph

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.ui.common.screen.GraphScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val graphFeatureModule = module {
    single(named("GraphPresenter")) {
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is GraphScreen -> GraphPresenter(
                    navigator = navigator,
                )

                else -> null
            }
        }
    }
    single(named("GraphScreen")) {
        Ui.Factory { screen, context ->
            when (screen) {
                is GraphScreen -> {
                    ui<GraphUiState> { state, modifier ->
                        GraphUi(
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
