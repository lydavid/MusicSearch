package ly.david.musicsearch.shared.domain.search.results

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

data class SearchResultMetadata(
    val entity: MusicBrainzEntityType,
    val query: String,
    val localCount: Int,
    val remoteCount: Int,
)
