package ly.david.data.spotify.di

import ly.david.data.spotify.ArtistImageRepository
import ly.david.data.spotify.auth.store.SpotifyAuthStore
import ly.david.data.spotify.auth.store.SpotifyAuthStoreImpl
import ly.david.data.spotify.auth.api.SpotifyOAuthApi
import ly.david.data.spotify.auth.api.SpotifyOAuthApiImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyApiModule = module {
    singleOf(::SpotifyOAuthApiImpl) bind SpotifyOAuthApi::class
    singleOf(::SpotifyAuthStoreImpl) bind SpotifyAuthStore::class
    singleOf(::ArtistImageRepository)
}
