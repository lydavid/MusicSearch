package ly.david.musicsearch.share.feature.database

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.share.feature.database.all.AllEntitiesPresenter
import ly.david.musicsearch.share.feature.database.all.AllEntitiesUi
import ly.david.musicsearch.share.feature.database.all.AllEntitiesUiState
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
                    imageMetadataRepository = get(),
                    areasListRepository = get(),
                    artistsListRepository = get(),
                    eventsListRepository = get(),
                    genresListRepository = get(),
                    instrumentsListRepository = get(),
                    labelsListRepository = get(),
                    placesListRepository = get(),
                    recordingsListRepository = get(),
                    releasesListRepository = get(),
                    releaseGroupsListRepository = get(),
                    seriesListRepository = get(),
                    worksListRepository = get(),
                )

                is AllEntitiesScreen -> AllEntitiesPresenter(
                    screen = screen,
                    navigator = navigator,
                    entitiesListPresenter = get(),
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
                    ui<AllEntitiesUiState> { state, modifier ->
                        AllEntitiesUi(
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
