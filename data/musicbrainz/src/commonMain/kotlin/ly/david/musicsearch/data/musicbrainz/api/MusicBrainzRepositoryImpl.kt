package ly.david.musicsearch.data.musicbrainz.api

import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository

/**
 * MusicBrainz base url for API and web.
 */
private const val MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org"

class MusicBrainzRepositoryImpl : MusicbrainzRepository {
    override fun getBaseUrl(): String {
        return MUSIC_BRAINZ_BASE_URL
    }
}
