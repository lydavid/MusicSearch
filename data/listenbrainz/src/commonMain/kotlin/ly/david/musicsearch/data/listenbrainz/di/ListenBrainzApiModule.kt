package ly.david.musicsearch.data.listenbrainz.di

import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApiImpl
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzRepositoryImpl
import ly.david.musicsearch.data.listenbrainz.auth.ListenBrainzStoreImpl
import ly.david.musicsearch.shared.domain.auth.ListenBrainzStore
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val listenBrainzApiModule = module {
    singleOf(::ListenBrainzApiImpl) bind ListenBrainzApi::class
    singleOf(::ListenBrainzStoreImpl) bind ListenBrainzStore::class
    singleOf(::ListenBrainzRepositoryImpl) bind ListenBrainzRepository::class
}
