package ly.david.musicsearch.data.spotify.di

import ly.david.musicsearch.data.spotify.api.SpotifyApi
import ly.david.musicsearch.data.spotify.auth.api.SpotifyOAuthApi
import ly.david.musicsearch.data.spotify.auth.api.SpotifyOAuthApiImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyApiModule = module {
    singleOf(::SpotifyOAuthApiImpl) bind SpotifyOAuthApi::class
    single {
        SpotifyApi.create(
            httpClient = get(),
            spotifyOAuthInfo = get(),
            spotifyOAuthApi = get(),
            spotifyAuthStore = get(),
        )
    }
}
