package ly.david.data.di.musicbrainz

import android.net.Uri
import ly.david.data.BuildConfig
import ly.david.data.core.AppInfo
import ly.david.data.musicbrainz.api.MUSIC_BRAINZ_BASE_URL
import ly.david.data.musicbrainz.auth.MusicBrainzOAuthInfo
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues
import org.koin.dsl.module

//@InstallIn(SingletonComponent::class)
//@Module
//object MusicBrainzAuthModule {
//
//    @Provides
//    fun provideOAuthInfo(): MusicBrainzOAuthInfo {
//        return MusicBrainzOAuthInfo(
//            clientId = BuildConfig.MUSICBRAINZ_CLIENT_ID,
//            clientSecret = BuildConfig.MUSICBRAINZ_CLIENT_SECRET
//        )
//    }
//
//    @Provides
//    fun provideAuthorizationService(
//        @ApplicationContext context: Context,
//    ): AuthorizationService {
//        return AuthorizationService(context)
//    }
//
//    @Provides
//    fun provideAuthorizationServiceConfiguration(): AuthorizationServiceConfiguration {
//        return AuthorizationServiceConfiguration(
//            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/authorize"),
//            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/token"),
//            null,
//            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/revoke") // Doesn't work cause GET revoke not implemented
//        )
//    }
//
//    @Provides
//    fun provideAuthorizationRequest(
//        serviceConfig: AuthorizationServiceConfiguration,
//        musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
//        appInfo: AppInfo,
//    ): AuthorizationRequest {
//        return AuthorizationRequest.Builder(
//            serviceConfig,
//            musicBrainzOAuthInfo.clientId,
//            ResponseTypeValues.CODE,
//            Uri.parse("${appInfo.applicationId}://oauth2/redirect")
//        )
//            .setScope("collection profile")
//            .build()
//    }
//
//    @Provides
//    fun provideClientAuthentication(
//        musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
//    ): ClientAuthentication {
//        return ClientSecretBasic(musicBrainzOAuthInfo.clientSecret)
//    }
//}

val musicBrainzAuthModule = module {
    single {
        MusicBrainzOAuthInfo(
            clientId = BuildConfig.MUSICBRAINZ_CLIENT_ID,
            clientSecret = BuildConfig.MUSICBRAINZ_CLIENT_SECRET
        )
    }

    factory<AuthorizationService> {
        AuthorizationService(get())
    }

    factory<AuthorizationServiceConfiguration> {
        AuthorizationServiceConfiguration(
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/authorize"),
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/token"),
            null,
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/revoke") // Doesn't work cause GET revoke not implemented
        )
    }

    factory<AuthorizationRequest> {
        AuthorizationRequest.Builder(
            get(),
            get<MusicBrainzOAuthInfo>().clientId,
            ResponseTypeValues.CODE,
            Uri.parse("${get<AppInfo>().applicationId}://oauth2/redirect")
        )
            .setScope("collection profile")
            .build()
    }

    factory<ClientAuthentication> {
        ClientSecretBasic(get<MusicBrainzOAuthInfo>().clientSecret)
    }
}
