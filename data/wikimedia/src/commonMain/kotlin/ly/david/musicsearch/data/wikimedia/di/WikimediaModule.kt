package ly.david.musicsearch.data.wikimedia.di

import ly.david.musicsearch.data.wikimedia.WikimediaRepositoryImpl
import ly.david.musicsearch.data.wikimedia.api.WikidataInstanceRepositoryImpl
import ly.david.musicsearch.data.wikimedia.api.WikimediaApi
import ly.david.musicsearch.data.wikimedia.api.WikimediaApiImpl
import ly.david.musicsearch.shared.domain.wikimedia.WikidataInstanceRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wikimediaModule = module {
    singleOf(::WikimediaApiImpl) bind WikimediaApi::class
    singleOf(::WikidataInstanceRepositoryImpl) bind WikidataInstanceRepository::class
    singleOf(::WikimediaRepositoryImpl) bind WikimediaRepository::class
}
