package ly.david.data.test.di

import ly.david.data.test.api.FakeCoverArtArchiveApi
import ly.david.data.test.api.FakeMusicBrainzApi
import ly.david.data.test.api.FakeSpotifyApi
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.spotify.api.SpotifyApi
import ly.david.musicsearch.data.spotify.auth.api.SpotifyOAuthApi
import ly.david.musicsearch.data.spotify.auth.api.SpotifyOAuthClientCredentialsResponse
import org.koin.dsl.module

val testApiModule = module {
    single<CoverArtArchiveApi> {
        FakeCoverArtArchiveApi()
    }

    single<MusicBrainzApi> {
        FakeMusicBrainzApi()
    }

    single<SpotifyOAuthApi> {
        object : SpotifyOAuthApi {
            override suspend fun getAccessToken(
                clientId: String,
                clientSecret: String,
                grantType: String,
            ): SpotifyOAuthClientCredentialsResponse {
                TODO("Not yet implemented")
            }
        }
    }

    single<SpotifyApi> {
        FakeSpotifyApi()
    }
}
