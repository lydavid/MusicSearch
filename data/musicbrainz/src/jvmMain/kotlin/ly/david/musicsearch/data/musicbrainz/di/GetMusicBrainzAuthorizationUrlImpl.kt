package ly.david.musicsearch.data.musicbrainz.di

import io.ktor.http.encodeURLPath
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import ly.david.musicsearch.shared.domain.auth.GetMusicBrainzAuthorizationUrl
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository

internal class GetMusicBrainzAuthorizationUrlImpl(
    private val musicbrainzRepository: MusicbrainzRepository,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
) : GetMusicBrainzAuthorizationUrl {
    override operator fun invoke(): String {
        return musicbrainzRepository.getAuthorizationEndpoint() +
            "?response_type=code" +
            "&client_id=${musicBrainzOAuthInfo.clientId}" +
            "&redirect_uri=urn%3Aietf%3Awg%3Aoauth%3A2.0%3Aoob" +
            "&scope=${musicBrainzOAuthInfo.scope.encodeURLPath()}"
    }
}
