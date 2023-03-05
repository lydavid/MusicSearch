package ly.david.data.di

import android.content.Context
import android.net.Uri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ly.david.data.network.api.MUSIC_BRAINZ_BASE_URL
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues

private const val MUSIC_BRAINZ_OAUTH_CLIENT_ID = "afBf7jhb_ms-Fjqm6VTWTvVAyuAUf_xT"

/**
 * Mobile apps embed secrets in their code so we don't have to hide this.
 */
private const val MUSIC_BRAINZ_OAUTH_CLIENT_SECRET = "nnkNItEfufwKj0-yjgmgZVrnzrXRQBN7"

@InstallIn(SingletonComponent::class)
@Module
internal object MusicBrainzAuthComponent {
    @Provides
    fun provideServiceConfig(): AuthorizationServiceConfiguration {
        return AuthorizationServiceConfiguration(
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/authorize"),
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/token")
        )
    }

    @Provides
    fun provideRequestBuilder(
        serviceConfig: AuthorizationServiceConfiguration
    ): AuthorizationRequest.Builder {
        return AuthorizationRequest.Builder(
            serviceConfig,
            MUSIC_BRAINZ_OAUTH_CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse("io.github.lydavid.musicsearch://oauth2/redirect")
        )
    }

    @Provides
    fun provideRequest(
        builder: AuthorizationRequest.Builder
    ): AuthorizationRequest {
        return builder
            .setScope("collection profile")
            .build()
    }

    @Provides
    fun provideAuthService(
        @ApplicationContext context: Context
    ): AuthorizationService {
        return AuthorizationService(context)
    }

    @Provides
    fun provideClientAuth(): ClientAuthentication {
        return ClientSecretBasic(MUSIC_BRAINZ_OAUTH_CLIENT_SECRET)
    }
}
