package ly.david.musicsearch.shared.domain.musicbrainz.usecase

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GetMusicBrainzCoverArtUrl(
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntity,
    ): String {
        val entityUrl = "${getMusicBrainzUrl(entity, entityId)}/"
        return when (entity) {
            MusicBrainzEntity.EVENT -> entityUrl + "event-art"
            MusicBrainzEntity.RELEASE -> entityUrl + "cover-art"
            else -> entityUrl
        }
    }
}
