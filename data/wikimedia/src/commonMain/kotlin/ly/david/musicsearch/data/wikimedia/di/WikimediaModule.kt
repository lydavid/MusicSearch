package ly.david.musicsearch.data.wikimedia.di

import ly.david.musicsearch.data.wikimedia.WikimediaRepositoryImpl
import ly.david.musicsearch.data.wikimedia.api.WikimediaApi
import ly.david.musicsearch.data.wikimedia.api.WikimediaApiImpl
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wikimediaModule = module {
    singleOf(::WikimediaApiImpl) bind WikimediaApi::class
    singleOf(::WikimediaRepositoryImpl) bind WikimediaRepository::class
}
