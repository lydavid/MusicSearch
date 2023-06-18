package ly.david.data.musicbrainz

import android.content.Context
import android.net.Uri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ly.david.data.network.api.MUSIC_BRAINZ_BASE_URL
import ly.david.data.network.api.MUSIC_BRAINZ_OAUTH_CLIENT_ID
import ly.david.data.network.api.MUSIC_BRAINZ_OAUTH_CLIENT_SECRET
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues

@InstallIn(SingletonComponent::class)
@Module
object MusicBrainzAuthModule {

    @Provides
    fun provideAuthorizationService(
        @ApplicationContext context: Context
    ): AuthorizationService {
        return AuthorizationService(context)
    }

    @Provides
    fun provideAuthorizationServiceConfiguration(): AuthorizationServiceConfiguration {
        return AuthorizationServiceConfiguration(
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/authorize"),
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/token"),
            null,
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/revoke") // Doesn't work cause GET revoke not implemented
        )
    }

    @Provides
    fun provideAuthorizationRequest(
        serviceConfig: AuthorizationServiceConfiguration
    ): AuthorizationRequest {
        return AuthorizationRequest.Builder(
            serviceConfig,
            MUSIC_BRAINZ_OAUTH_CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse("io.github.lydavid.musicsearch://oauth2/redirect")
        )
            .setScope("collection profile")
            .build()
    }

    @Provides
    fun provideClientAuthentication(): ClientAuthentication {
        return ClientSecretBasic(MUSIC_BRAINZ_OAUTH_CLIENT_SECRET)
    }
}
