package ly.david.musicsearch.shared.feature.stats

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.ui.common.screen.StatsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val statsFeatureModule = module {
    single(named("StatsScreen")) {
        Presenter.Factory { screen, _, _ ->
            when (screen) {
                is StatsScreen -> {
                    StatsPresenter(
                        screen = screen,
                        getCountOfEachRelationshipTypeUseCase = get(),
                        browseRemoteMetadataRepository = get(),
                        entitiesListRepository = get(),
                        releaseGroupsListRepository = get(),
                    )
                }

                else -> null
            }
        }
    }
    single(named("StatsScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is StatsScreen -> {
                    ui<StatsUiState> { state, modifier ->
                        StatsUi(
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
