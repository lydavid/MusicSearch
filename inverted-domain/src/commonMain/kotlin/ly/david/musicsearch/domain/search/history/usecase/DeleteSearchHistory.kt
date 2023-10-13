package ly.david.musicsearch.domain.search.history.usecase

import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.search.history.SearchHistoryRepository
import org.koin.core.annotation.Single

@Single
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
            searchHistoryRepository.delete(entity, query)
        }
    }
}
