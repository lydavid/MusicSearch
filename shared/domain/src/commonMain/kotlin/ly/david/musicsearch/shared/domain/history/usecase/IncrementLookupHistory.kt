package ly.david.musicsearch.shared.domain.history.usecase

import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository

class IncrementLookupHistory(
    private val lookupHistoryRepository: LookupHistoryRepository,
) {
    operator fun invoke(lookupHistory: LookupHistory) {
        lookupHistoryRepository.upsert(lookupHistory)
    }
}
