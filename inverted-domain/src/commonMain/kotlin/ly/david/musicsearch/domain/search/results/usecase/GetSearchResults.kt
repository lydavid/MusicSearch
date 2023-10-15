package ly.david.musicsearch.domain.search.results.usecase

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.search.results.SearchResultsRepository
import org.koin.core.annotation.Single

@Single
class GetSearchResults(
    private val searchResultsRepository: SearchResultsRepository,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
        query: String,
    ) = searchResultsRepository.observeSearchResults(entity, query)
}
