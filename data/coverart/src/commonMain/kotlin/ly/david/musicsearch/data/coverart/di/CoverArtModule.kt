package ly.david.musicsearch.data.coverart.di

import ly.david.musicsearch.data.coverart.ImageMetadataRepositoryImpl
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApiImpl
import ly.david.musicsearch.shared.domain.release.ImageMetadataRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coverArtModule = module {
    singleOf(::CoverArtArchiveApiImpl) bind CoverArtArchiveApi::class
    singleOf(::ImageMetadataRepositoryImpl) bind ImageMetadataRepository::class
}
