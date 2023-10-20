package ly.david.data.di.network

import ly.david.data.BuildConfig
import ly.david.musicsearch.data.spotify.api.SpotifyApi
import org.koin.dsl.module

val networkModule = module {
    single {
        SpotifyApi.create(
            httpClient = get(),
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET,
            spotifyOAuthApi = get(),
            spotifyAuthStore = get(),
        )
    }
}
