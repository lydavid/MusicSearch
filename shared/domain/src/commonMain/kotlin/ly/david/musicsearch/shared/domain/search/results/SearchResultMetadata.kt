package ly.david.musicsearch.shared.domain.search.results

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

data class SearchResultMetadata(
    val entity: MusicBrainzEntity,
    val query: String,
    val localCount: Int,
    val remoteCount: Int,
)
