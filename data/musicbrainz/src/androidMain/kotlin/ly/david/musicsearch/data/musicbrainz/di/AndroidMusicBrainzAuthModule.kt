package ly.david.musicsearch.data.musicbrainz.di

import android.net.Uri
import ly.david.musicsearch.core.models.AppInfo
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_BASE_URL
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues
import org.koin.core.module.Module
import org.koin.dsl.module

actual val musicBrainzAuthModule: Module = module {
    factory<AuthorizationService> {
        AuthorizationService(/* context = */ get())
    }

    factory<AuthorizationRequest> {
        AuthorizationRequest.Builder(
            /* configuration = */
            AuthorizationServiceConfiguration(
                /* authorizationEndpoint = */
                // TODO: these endpoints should be supplied by a singleton in commonMain
                //  so that android and jvm can use for their own implementations
                Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/authorize"),
                /* tokenEndpoint = */
                Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/token"),
                /* registrationEndpoint = */
                null,
                /* endSessionEndpoint = */
                Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/revoke"), // Doesn't work cause GET revoke not implemented
            ),
            /* clientId = */
            get<MusicBrainzOAuthInfo>().clientId,
            /* responseType = */
            ResponseTypeValues.CODE,
            /* redirectUri = */
            Uri.parse("${get<AppInfo>().applicationId}://oauth2/redirect"),
        )
            .setScope("collection profile")
            .build()
    }

    factory<ClientAuthentication> {
        ClientSecretBasic(get<MusicBrainzOAuthInfo>().clientSecret)
    }
}
