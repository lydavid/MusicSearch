package ly.david.musicsearch.shared.feature.search

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.shared.feature.search.internal.SearchPresenter
import ly.david.musicsearch.shared.feature.search.internal.Search
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchFeatureModule: Module = module {
    single(named(SearchPresenter::class.java.name)) {
        Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is SearchScreen -> SearchPresenter(
                    screen = screen,
                    navigator = navigator,
                    getSearchResults = get(),
                    getSearchHistory = get(),
                    recordSearchHistory = get(),
                    deleteSearchHistory = get(),
                )

                else -> null
            }
        }
    }
    single(named(SearchScreen::class.java.name)) {
        Ui.Factory { screen, context ->
            when (screen) {
                is SearchScreen -> {
                    ui<SearchScreen.UiState> { state, modifier ->
                        Search(
                            uiState = state,
                            modifier = modifier,
                        )
                    }
                }

                else -> null
            }
        }
    }
}
