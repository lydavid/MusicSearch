package ly.david.musicsearch.data.musicbrainz.di

import MusicSearch.data.musicbrainz.BuildConfig
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_OAUTH_SCOPE
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthRepository
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthStoreImpl
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val musicBrainzDataModule = module {
    single {
        val baseUrl = get<MusicbrainzRepository>().getBaseUrl()
        val oauthBaseUrl = "$baseUrl/oauth2"
        MusicBrainzOAuthInfo(
            clientId = BuildConfig.MUSICBRAINZ_CLIENT_ID,
            clientSecret = BuildConfig.MUSICBRAINZ_CLIENT_SECRET,
            oauthBaseUrl = oauthBaseUrl,
            authorizationEndpoint = "$oauthBaseUrl/authorize",
            tokenEndpoint = "$oauthBaseUrl/token",
            endSessionEndpoint = "$oauthBaseUrl/revoke",
            scope = MUSIC_BRAINZ_OAUTH_SCOPE,
        )
    }
    singleOf(::MusicBrainzAuthStoreImpl) bind MusicBrainzAuthStore::class
    singleOf(::MusicBrainzAuthRepository)
}
