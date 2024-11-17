package ly.david.musicsearch.shared.domain.musicbrainz.usecase

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GetMusicBrainzCoverArtUrl(
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
) {
    operator fun invoke(
        entityId: String,
    ): String {
        return "${getMusicBrainzUrl(MusicBrainzEntity.RELEASE, entityId)}/cover-art"
    }
}
