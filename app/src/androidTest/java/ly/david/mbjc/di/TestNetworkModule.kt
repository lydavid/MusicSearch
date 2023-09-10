package ly.david.mbjc.di

import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.auth.SpotifyOAuthApi
import ly.david.data.spotify.api.auth.SpotifyOAuthClientCredentialsResponse
import ly.david.data.test.api.FakeCoverArtArchiveApi
import ly.david.data.test.api.FakeMusicBrainzApi
import ly.david.data.test.api.FakeSpotifyApi
import org.koin.dsl.module

// TODO: these are test modules, while what they provide are fakes
//  fake module doesn't make that much sense cause they are real modules but for tests

val testNetworkModule = module {
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
