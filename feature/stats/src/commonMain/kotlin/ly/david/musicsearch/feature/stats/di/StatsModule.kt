package ly.david.musicsearch.feature.stats.di

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.feature.stats.StatsScreen
import ly.david.musicsearch.feature.stats.circuit.ReleaseStatsPresenter
import ly.david.musicsearch.feature.stats.circuit.ReleaseStatsScreen
import org.koin.dsl.module

val statsFeatureModule = module {
    factory { //args ->
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is ReleaseStatsScreen -> ReleaseStatsPresenter(screen, navigator, get())
                else -> null
            }
        }
    }
    factory {
        Ui.Factory { screen, context ->
            when (screen) {
                is ReleaseStatsScreen -> {
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
    single {
        Circuit.Builder()
//            .addUiFactory(AddFavoritesUiFactory())
//            .addPresenterFactory(AddFavoritesPresenterFactory())
//            .addPresenter<ReleaseStatsScreen, ReleaseStatsScreen.State>(ReleaseStatsPresenter(getCountOfEachRelationshipTypeUseCase))
//            .addUi<ReleaseStatsScreen, ReleaseStatsScreen.State> { state, modifier ->
//                ReleaseStatsUi(state, modifier)
//            }
            .addPresenterFactory(get())
            .addUiFactory(get())
            .build()
    }
}
