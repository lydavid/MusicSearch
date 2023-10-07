package ly.david.musicsearch.domain.history

import ly.david.data.core.history.LookupHistory
import org.koin.core.annotation.Single

@Single
class IncrementLookupHistory(
    private val lookupHistoryRepository: LookupHistoryRepository,
) {
    operator fun invoke(lookupHistory: LookupHistory) {
        lookupHistoryRepository.upsert(lookupHistory)
    }
}
