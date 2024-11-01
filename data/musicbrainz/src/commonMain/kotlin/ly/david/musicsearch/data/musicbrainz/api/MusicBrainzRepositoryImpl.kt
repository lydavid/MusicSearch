package ly.david.musicsearch.data.musicbrainz.api

import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_BASE_URL
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository

class MusicBrainzRepositoryImpl : MusicbrainzRepository {
    override fun getBaseUrl(): String {
        return MUSIC_BRAINZ_BASE_URL
    }
}
