package ly.david.musicsearch.data.spotify.di

import ly.david.musicsearch.data.spotify.ArtistImageRepository
import ly.david.musicsearch.data.spotify.auth.api.SpotifyOAuthApi
import ly.david.musicsearch.data.spotify.auth.api.SpotifyOAuthApiImpl
import ly.david.musicsearch.data.spotify.auth.store.SpotifyAuthStore
import ly.david.musicsearch.data.spotify.auth.store.SpotifyAuthStoreImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyDataModule = module {
    singleOf(::SpotifyOAuthApiImpl) bind SpotifyOAuthApi::class
    singleOf(::SpotifyAuthStoreImpl) bind SpotifyAuthStore::class
    singleOf(::ArtistImageRepository)
}
