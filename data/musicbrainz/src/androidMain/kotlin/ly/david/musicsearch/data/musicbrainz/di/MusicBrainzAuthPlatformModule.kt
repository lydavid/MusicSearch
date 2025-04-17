package ly.david.musicsearch.data.musicbrainz.di

import ly.david.musicsearch.data.musicbrainz.auth.LoginAndroidImpl
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzLoginActivityResultContractImpl
import ly.david.musicsearch.shared.domain.auth.LoginAndroid
import ly.david.musicsearch.shared.domain.auth.MusicBrainzLoginActivityResultContract
import net.openid.appauth.AuthorizationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val musicBrainzAuthPlatformModule = module {
    single { AuthorizationService(get()) }
    singleOf(::MusicBrainzLoginActivityResultContractImpl) bind MusicBrainzLoginActivityResultContract::class
    singleOf(::LoginAndroidImpl) bind LoginAndroid::class
}
