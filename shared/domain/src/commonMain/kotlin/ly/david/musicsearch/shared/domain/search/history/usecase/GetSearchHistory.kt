package ly.david.musicsearch.shared.domain.search.history.usecase

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.history.SearchHistoryRepository

class GetSearchHistory(
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
    ) = searchHistoryRepository.observeSearchHistory(entity)
}
