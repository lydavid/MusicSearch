package ly.david.musicsearch.domain.search.history.usecase

import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.search.history.SearchHistoryRepository
import org.koin.core.annotation.Single

@Single
class GetSearchHistory(
    private val searchHistoryRepository: SearchHistoryRepository,
) {
    operator fun invoke(
        entity: MusicBrainzEntity,
    ) = searchHistoryRepository.observeSearchHistory(entity)
}
