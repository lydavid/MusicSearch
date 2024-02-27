package ly.david.musicsearch.data.musicbrainz.di

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.builder.api.DefaultApi20
import com.github.scribejava.core.model.OAuthConstants
import com.github.scribejava.core.oauth.OAuth20Service
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import org.koin.dsl.module

actual val musicBrainzAuthPlatformModule = module {
    single<OAuth20Service> {
        val musicBrainzOAuthInfo = get<MusicBrainzOAuthInfo>()
        val musicBrainzApi20 = object : DefaultApi20() {
            override fun getAccessTokenEndpoint(): String = musicBrainzOAuthInfo.tokenEndpoint
            override fun getAuthorizationBaseUrl(): String = musicBrainzOAuthInfo.authorizationEndpoint
        }
        ServiceBuilder(musicBrainzOAuthInfo.clientId)
            .callback("urn:ietf:wg:oauth:2.0:oob")
            .defaultScope(musicBrainzOAuthInfo.scope)
            .responseType(OAuthConstants.CODE)
            .apiSecret(musicBrainzOAuthInfo.clientSecret)
            .build(musicBrainzApi20)
    }
}
