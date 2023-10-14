package ly.david.musicsearch.data.repository.di

import ly.david.musicsearch.data.repository.AreaRepositoryImpl
import ly.david.musicsearch.data.repository.ArtistRepositoryImpl
import ly.david.musicsearch.data.repository.LookupHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.SearchHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.SearchResultsRepositoryImpl
import ly.david.musicsearch.domain.area.AreaRepository
import ly.david.musicsearch.domain.artist.ArtistRepository
import ly.david.musicsearch.domain.history.LookupHistoryRepository
import ly.david.musicsearch.domain.relation.RelationRepository
import ly.david.musicsearch.domain.search.history.SearchHistoryRepository
import ly.david.musicsearch.domain.search.results.SearchResultsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryDataModule = module {
    singleOf(::AreaRepositoryImpl) bind AreaRepository::class
    singleOf(::ArtistRepositoryImpl) bind ArtistRepository::class
    singleOf(::LookupHistoryRepositoryImpl) bind LookupHistoryRepository::class
    singleOf(::RelationRepositoryImpl) bind RelationRepository::class
    singleOf(::SearchResultsRepositoryImpl) bind SearchResultsRepository::class
    singleOf(::SearchHistoryRepositoryImpl) bind SearchHistoryRepository::class
}
