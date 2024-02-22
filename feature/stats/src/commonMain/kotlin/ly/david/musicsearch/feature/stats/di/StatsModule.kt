package ly.david.musicsearch.feature.stats.di

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.feature.stats.StatsScreen
import ly.david.musicsearch.feature.stats.circuit.ReleaseStatsPresenter
import ly.david.musicsearch.feature.stats.circuit.ReleaseStatsScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val statsFeatureModule = module {
    single(named(ReleaseStatsPresenter::class.java.name)) {
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is ReleaseStatsScreen -> ReleaseStatsPresenter(
                    screen = screen,
                    navigator = navigator,
                    getCountOfEachRelationshipTypeUseCase = get(),
                )

                else -> null
            }
        }
    }
    single(named(ReleaseStatsScreen::class.java.name)) {
        Ui.Factory { screen, context ->
            when (screen) {
                is ReleaseStatsScreen -> {
                    // TODO: Cannot inline bytecode built with JVM target 11 into bytecode that is being built
                    //  with JVM target 1.8. Please specify proper '-jvm-target' option
                    ui<ReleaseStatsScreen.State> { state, modifier ->
                        StatsScreen(
                            modifier = modifier,
                            tabs = state.tabs.toImmutableList(),
                            stats = state.stats,
                        )
                    }
                }

                else -> null
            }
        }
    }
}
