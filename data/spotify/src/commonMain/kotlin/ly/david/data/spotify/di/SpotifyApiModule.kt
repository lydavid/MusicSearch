package ly.david.data.spotify.di

import ly.david.data.spotify.api.auth.SpotifyOAuthApi
import org.koin.dsl.module

val spotifyApiModule = module {
    single {
        SpotifyOAuthApi.create(
            httpClient = get(),
        )
    }
}
