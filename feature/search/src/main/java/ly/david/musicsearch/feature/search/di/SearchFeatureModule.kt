package ly.david.musicsearch.feature.search.di

import ly.david.musicsearch.feature.search.internal.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val searchFeatureModule = module {
    viewModelOf(::SearchViewModel)
}
