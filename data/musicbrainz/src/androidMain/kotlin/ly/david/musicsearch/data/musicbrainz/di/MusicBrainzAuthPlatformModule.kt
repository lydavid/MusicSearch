package ly.david.musicsearch.data.musicbrainz.di

import ly.david.musicsearch.data.musicbrainz.auth.LoginAndroid
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzLoginActivityResultContract
import net.openid.appauth.AuthorizationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val musicBrainzAuthPlatformModule = module {
    single { AuthorizationService(get()) }
    singleOf(::MusicBrainzLoginActivityResultContract)
    singleOf(::LoginAndroid)
}
