package ly.david.musicsearch.data.repository.di

import ly.david.musicsearch.data.repository.LookupHistoryRepositoryImpl
import ly.david.musicsearch.domain.history.LookupHistoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryDataModule = module {
    singleOf(::LookupHistoryRepositoryImpl) bind LookupHistoryRepository::class
}
