package ly.david.data.spotify.di

import ly.david.data.spotify.api.auth.SpotifyAuthStore
import ly.david.data.spotify.api.auth.SpotifyOAuthApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyApiModule = module {
    single {
        SpotifyOAuthApi.create(
            httpClient = get(),
        )
    }
    singleOf(::SpotifyAuthStoreImpl) bind SpotifyAuthStore::class
}
