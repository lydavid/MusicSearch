package ly.david.musicsearch.shared.domain.search.results.usecase

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository

class GetSearchResults(
    private val searchResultsRepository: SearchResultsRepository,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
        query: String,
    ) = searchResultsRepository.observeSearchResults(
        entity,
        query,
    )
}
