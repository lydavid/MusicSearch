package ly.david.musicsearch.share.feature.database

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.share.feature.database.all.AllLocalEntitiesPresenter
import ly.david.musicsearch.share.feature.database.all.AllLocalEntitiesUi
import ly.david.musicsearch.share.feature.database.all.AllLocalEntitiesUiState
import ly.david.musicsearch.ui.common.screen.AllEntitiesScreen
import ly.david.musicsearch.ui.common.screen.DatabaseScreen
import org.koin.core.qualifier.named
import org.koin.dsl.module

val databaseFeatureModule = module {
    single(named("DatabasePresenter")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is DatabaseScreen -> DatabasePresenter(
                    navigator = navigator,
                    musicBrainzImageMetadataRepository = get(),
                    entitiesListRepository = get(),
                )

                is AllEntitiesScreen -> AllLocalEntitiesPresenter(
                    screen = screen,
                    navigator = navigator,
                    allEntitiesListPresenter = get(),
                    loginPresenter = get(),
                )

                else -> null
            }
        }
    }
    single(named("DatabaseScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is DatabaseScreen -> {
                    ui<DatabaseUiState> { state, modifier ->
                        DatabaseUi(
                            state = state,
                            modifier = modifier,
                        )
                    }
                }

                is AllEntitiesScreen -> {
                    ui<AllLocalEntitiesUiState> { state, modifier ->
                        AllLocalEntitiesUi(
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
