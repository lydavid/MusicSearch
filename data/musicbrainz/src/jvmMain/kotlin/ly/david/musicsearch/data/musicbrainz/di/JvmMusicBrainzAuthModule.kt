package ly.david.musicsearch.data.musicbrainz.di

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.model.OAuthConstants
import com.github.scribejava.core.oauth.OAuth20Service
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import org.koin.core.module.Module
import org.koin.dsl.module

actual val musicBrainzAuthModule: Module = module {
    single {
        val musicBrainzOAuthInfo = get<MusicBrainzOAuthInfo>()
        val musicBrainzApi20 = object : DefaultApi20() {
            override fun getAccessTokenEndpoint(): String {
                return musicBrainzOAuthInfo.tokenEndpoint
            }
            override fun getAuthorizationBaseUrl(): String {
                return musicBrainzOAuthInfo.authorizationEndpoint
            }
        }

        val service: OAuth20Service = ServiceBuilder(musicBrainzOAuthInfo.clientId)
            .callback("urn:ietf:wg:oauth:2.0:oob")
            .scope("collection profile")
            .responseType(OAuthConstants.CODE)
            .apiSecret(musicBrainzOAuthInfo.clientSecret)
            .build(musicBrainzApi20)

        service
    }
}
