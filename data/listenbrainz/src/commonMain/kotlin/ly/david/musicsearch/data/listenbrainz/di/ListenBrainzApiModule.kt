package ly.david.musicsearch.data.listenbrainz.di

import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzRepositoryImpl
import ly.david.musicsearch.data.listenbrainz.auth.ListenBrainzAuthStoreImpl
import ly.david.musicsearch.data.listenbrainz.auth.UpdateListenBrainzTokenImpl
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.UpdateListenBrainzToken
import ly.david.musicsearch.shared.domain.listen.ListenBrainzRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val listenBrainzApiModule = module {
    single {
        ListenBrainzApi.create(
            httpClient = get(),
            authStore = get(),
        )
    } bind ListenBrainzApi::class
    singleOf(::ListenBrainzAuthStoreImpl) bind ListenBrainzAuthStore::class
    singleOf(::ListenBrainzRepositoryImpl) bind ListenBrainzRepository::class
    singleOf(::UpdateListenBrainzTokenImpl) bind UpdateListenBrainzToken::class
}
