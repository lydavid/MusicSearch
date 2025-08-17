package ly.david.musicsearch.shared.domain.musicbrainz.usecase

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class GetMusicBrainzCoverArtUrl(
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
) {
    operator fun invoke(
        entityId: String,
        entity: MusicBrainzEntityType,
    ): String {
        val entityUrl = "${getMusicBrainzUrl(entity, entityId)}/"
        return when (entity) {
            MusicBrainzEntityType.EVENT -> entityUrl + "event-art"
            MusicBrainzEntityType.RELEASE -> entityUrl + "cover-art"
            else -> entityUrl
        }
    }
}
