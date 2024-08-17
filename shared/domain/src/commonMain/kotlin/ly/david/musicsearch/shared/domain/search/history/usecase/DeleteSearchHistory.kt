package ly.david.musicsearch.shared.domain.search.history.usecase

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.history.SearchHistoryRepository

class DeleteSearchHistory(
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
        query: String = "",
    ) {
        if (query.isEmpty()) {
            searchHistoryRepository.deleteAll(entity)
        } else {
            searchHistoryRepository.delete(
                entity,
                query,
            )
        }
    }
}
