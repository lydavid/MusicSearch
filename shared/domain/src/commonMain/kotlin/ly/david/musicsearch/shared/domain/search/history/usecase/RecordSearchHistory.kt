package ly.david.musicsearch.shared.domain.search.history.usecase

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.search.history.SearchHistoryRepository

class RecordSearchHistory(
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    operator fun invoke(
        entity: MusicBrainzEntityType,
        query: String,
    ) {
        searchHistoryRepository.recordSearchHistory(
            entity,
            query,
        )
    }
}
