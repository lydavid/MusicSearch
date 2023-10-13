package ly.david.musicsearch.data.repository.di

import ly.david.musicsearch.data.repository.LookupHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.SearchHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.SearchResultsRepositoryImpl
import ly.david.musicsearch.domain.history.LookupHistoryRepository
import ly.david.musicsearch.domain.search.history.SearchHistoryRepository
import ly.david.musicsearch.domain.search.results.SearchResultsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryDataModule = module {
    singleOf(::LookupHistoryRepositoryImpl) bind LookupHistoryRepository::class
    singleOf(::SearchResultsRepositoryImpl) bind SearchResultsRepository::class
    singleOf(::SearchHistoryRepositoryImpl) bind SearchHistoryRepository::class
}
