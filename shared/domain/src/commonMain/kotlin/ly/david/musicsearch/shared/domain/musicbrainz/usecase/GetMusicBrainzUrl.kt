package ly.david.musicsearch.shared.domain.musicbrainz.usecase

import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.resourceUri

interface GetMusicBrainzUrl {
    operator fun invoke(
        entity: MusicBrainzEntityType,
        entityId: String,
    ): String
}

class GetMusicBrainzUrlImpl(
    private val musicbrainzRepository: MusicbrainzRepository,
) : GetMusicBrainzUrl {
    override operator fun invoke(
        entity: MusicBrainzEntityType,
        entityId: String,
    ): String {
        return "${musicbrainzRepository.getBaseUrl()}/${entity.resourceUri}/$entityId"
    }
}
