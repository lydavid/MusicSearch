package ly.david.musicsearch.data.musicbrainz.di

import ly.david.musicsearch.data.musicbrainz.auth.LoginImpl
import ly.david.musicsearch.shared.domain.auth.GetMusicBrainzAuthorizationUrl
import ly.david.musicsearch.shared.domain.auth.Login
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val musicBrainzAuthPlatformModule = module {
    singleOf(::GetMusicBrainzAuthorizationUrlImpl) bind GetMusicBrainzAuthorizationUrl::class
    singleOf(::LoginImpl) bind Login::class
}
