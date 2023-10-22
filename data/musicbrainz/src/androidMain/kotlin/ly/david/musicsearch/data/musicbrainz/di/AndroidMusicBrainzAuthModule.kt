package ly.david.musicsearch.data.musicbrainz.di

import android.net.Uri
import ly.david.musicsearch.core.models.AppInfo
import ly.david.musicsearch.data.musicbrainz.Login
import ly.david.musicsearch.data.musicbrainz.Logout
import ly.david.musicsearch.data.musicbrainz.MusicBrainzLoginActivityResultContract
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.ResponseTypeValues
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val musicBrainzAuthModule: Module = module {
    single { AuthorizationService(get()) }

    single<AuthorizationRequest> {
        val musicBrainzOAuthInfo = get<MusicBrainzOAuthInfo>()
        AuthorizationRequest.Builder(
            /* configuration = */
            AuthorizationServiceConfiguration(
                /* authorizationEndpoint = */
                Uri.parse(musicBrainzOAuthInfo.authorizationEndpoint),
                /* tokenEndpoint = */
                Uri.parse(musicBrainzOAuthInfo.tokenEndpoint),
                /* registrationEndpoint = */
                null,
                /* endSessionEndpoint = */
                Uri.parse(musicBrainzOAuthInfo.endSessionEndpoint), // Doesn't work cause GET revoke not implemented
            ),
            /* clientId = */
            musicBrainzOAuthInfo.clientId,
            /* responseType = */
            ResponseTypeValues.CODE,
            /* redirectUri = */
            Uri.parse("${get<AppInfo>().applicationId}://oauth2/redirect"),
        )
            .setScope(musicBrainzOAuthInfo.scope)
            .build()
    }

    singleOf(::MusicBrainzLoginActivityResultContract)
    single<ClientAuthentication> { ClientSecretBasic(get<MusicBrainzOAuthInfo>().clientSecret) }
    singleOf(::Login)
    singleOf(::Logout)
}
