package ly.david.musicsearch.shared.domain.musicbrainz.usecase

import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUri

class GetMusicBrainzUrl(
    private val musicbrainzRepository: MusicbrainzRepository,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
        entityId: String,
    ): String {
        return "${musicbrainzRepository.getBaseUrl()}/${entity.resourceUri}/$entityId"
    }
}
