package ly.david.musicsearch.domain.history.usecase

import ly.david.musicsearch.data.core.history.LookupHistory
import ly.david.musicsearch.domain.history.LookupHistoryRepository
import org.koin.core.annotation.Single

@Single
class IncrementLookupHistory(
    private val lookupHistoryRepository: LookupHistoryRepository,
) {
    operator fun invoke(lookupHistory: LookupHistory) {
        lookupHistoryRepository.upsert(lookupHistory)
    }
}
