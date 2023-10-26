package ly.david.musicsearch.feature.search.di

import ly.david.musicsearch.feature.search.internal.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

actual val searchFeatureModule = module {
    viewModelOf(::SearchViewModel)
//    single {
//        Presenter.Factory { screen, navigator, context ->
//            SearchPresenter(
//                get(),
//                get(),
//                get(),
//                get(),
//            )
//        }
//    } bind Presenter.Factory::class
//    single {
//        Ui.Factory { screen, context ->
//            when (screen) {
//                is SearchScreen -> {
//                    ui<SearchUiState> { state, modifier ->
//                        Text("hello ${state.query}")
//                    }
//                }
//                else -> null
//            }
//        }
//    } bind Ui.Factory::class
}
