package ly.david.musicsearch.data.spotify.di

import MusicSearch.data.spotify.BuildConfig
import ly.david.musicsearch.data.spotify.ArtistImageRepositoryImpl
import ly.david.musicsearch.data.spotify.auth.SpotifyOAuthInfo
import ly.david.musicsearch.data.spotify.auth.store.SpotifyAuthStore
import ly.david.musicsearch.data.spotify.auth.store.SpotifyAuthStoreImpl
import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyDataModule = module {
    single {
        SpotifyOAuthInfo(
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET,
        )
    }
    singleOf(::SpotifyAuthStoreImpl) bind SpotifyAuthStore::class
    singleOf(::ArtistImageRepositoryImpl) bind ArtistImageRepository::class
}
