package ly.david.musicsearch.shared.feature.search

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.search.url.LookupUrlPresenter
import ly.david.musicsearch.shared.feature.search.url.LookupUrlUi
import ly.david.musicsearch.shared.feature.search.url.LookupUrlUiState
import ly.david.musicsearch.ui.common.screen.LookupUrlScreen
import ly.david.musicsearch.ui.common.screen.SearchScreen
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchFeatureModule: Module = module {
    single(named("SearchPresenter")) {
        Presenter.Factory { screen, navigator, _ ->
            when (screen) {
                is SearchScreen -> SearchPresenter(
                    screen = screen,
                    navigator = navigator,
                    getSearchResults = get(),
                    getSearchHistory = get(),
                    recordSearchHistory = get(),
                    deleteSearchHistory = get(),
                    appPreferences = get(),
                    musicBrainzLoginPresenter = get(),
                )

                is LookupUrlScreen -> LookupUrlPresenter(
                    screen = screen,
                    navigator = navigator,
                    appPreferences = get(),
                    lookupUrlRepository = get(),
                )

                else -> null
            }
        }
    }

    single(named("SearchScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is SearchScreen -> {
                    ui<SearchUiState> { state, modifier ->
                        SearchUi(
                            state = state,
                            modifier = modifier,
                        )
                    }
                }

                is LookupUrlScreen -> {
                    ui<LookupUrlUiState> { state, modifier ->
                        LookupUrlUi(
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
