package ly.david.musicsearch.data.musicbrainz.di

import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_OAUTH_AUTHORIZATION_URL
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_OAUTH_SCOPE
import ly.david.musicsearch.data.musicbrainz.auth.LoginImpl
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import ly.david.musicsearch.shared.domain.auth.Login
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthorizationUrl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val musicBrainzAuthPlatformModule = module {
    single<MusicBrainzAuthorizationUrl> {
        val musicBrainzOAuthInfo = get<MusicBrainzOAuthInfo>()
        MusicBrainzAuthorizationUrl(
            url = MUSIC_BRAINZ_OAUTH_AUTHORIZATION_URL +
                "?response_type=code" +
                "&client_id=${musicBrainzOAuthInfo.clientId}" +
                "&redirect_uri=urn%3Aietf%3Awg%3Aoauth%3A2.0%3Aoob" +
                "&scope=$MUSIC_BRAINZ_OAUTH_SCOPE",
        )
    }
    singleOf(::LoginImpl) bind Login::class
}
