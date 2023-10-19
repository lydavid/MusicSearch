package ly.david.musicsearch.data.spotify.di

import ly.david.musicsearch.data.spotify.auth.api.SpotifyOAuthApi
import ly.david.musicsearch.data.spotify.auth.api.SpotifyOAuthApiImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyApiModule = module {
    singleOf(::SpotifyOAuthApiImpl) bind SpotifyOAuthApi::class
}
