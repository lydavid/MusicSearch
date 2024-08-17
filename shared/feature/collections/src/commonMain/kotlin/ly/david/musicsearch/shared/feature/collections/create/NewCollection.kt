package ly.david.musicsearch.shared.feature.collections.create

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

internal data class NewCollection(
    val name: String? = null,
    val entity: MusicBrainzEntity? = null,
)
