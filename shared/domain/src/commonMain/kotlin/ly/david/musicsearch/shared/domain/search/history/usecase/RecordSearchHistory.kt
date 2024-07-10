package ly.david.musicsearch.shared.domain.search.history.usecase

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.history.SearchHistoryRepository

class RecordSearchHistory(
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
        query: String,
    ) {
        searchHistoryRepository.recordSearchHistory(
            entity,
            query,
        )
    }
}
