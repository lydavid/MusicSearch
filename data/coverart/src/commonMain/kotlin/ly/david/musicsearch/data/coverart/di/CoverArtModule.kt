package ly.david.musicsearch.data.coverart.di

import ly.david.musicsearch.data.coverart.ReleaseGroupImageRepositoryImpl
import ly.david.musicsearch.data.coverart.ReleaseImageRepositoryImpl
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApiImpl
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupImageRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coverArtModule = module {
    singleOf(::CoverArtArchiveApiImpl) bind CoverArtArchiveApi::class
    singleOf(::ReleaseImageRepositoryImpl) bind ReleaseImageRepository::class
    singleOf(::ReleaseGroupImageRepositoryImpl) bind ReleaseGroupImageRepository::class
}
