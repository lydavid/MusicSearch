package ly.david.musicsearch.feature.stats

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.feature.stats.area.AreaStatsPresenter
import ly.david.musicsearch.feature.stats.internal.StatsUiState
import ly.david.ui.common.screen.AreaStatsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val statsFeatureModule = module {
    single(named(AreaStatsScreen::class.java.name)) {
        Presenter.Factory { screen, _, _ ->
            when (screen) {
                is AreaStatsScreen -> {
                    AreaStatsPresenter(
                        screen = screen,
                        getCountOfEachRelationshipTypeUseCase = get(),
                        observeBrowseEntityCount = get(),
                        releaseCountryDao = get(),
                        areaPlaceDao = get(),
                    )
                }

                else -> null
            }
        }
    }
    single(named(AreaStatsScreen::class.java.name)) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is AreaStatsScreen -> {
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
