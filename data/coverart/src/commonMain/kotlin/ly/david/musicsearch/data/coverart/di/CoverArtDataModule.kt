package ly.david.musicsearch.data.coverart.di

import ly.david.musicsearch.data.coverart.ReleaseGroupImageRepository
import ly.david.musicsearch.data.coverart.ReleaseGroupImageRepositoryImpl
import ly.david.musicsearch.data.coverart.ReleaseImageRepository
import ly.david.musicsearch.data.coverart.ReleaseImageRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coverArtDataModule = module {
    singleOf(::ReleaseImageRepositoryImpl) bind ReleaseImageRepository::class
    singleOf(::ReleaseGroupImageRepositoryImpl) bind ReleaseGroupImageRepository::class
}
