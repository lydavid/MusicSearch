package ly.david.musicsearch.shared.domain.history.usecase

import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository
import org.koin.core.annotation.Single

@Single
class IncrementLookupHistory(
    private val lookupHistoryRepository: LookupHistoryRepository,
) {
    operator fun invoke(lookupHistory: LookupHistory) {
        lookupHistoryRepository.upsert(lookupHistory)
    }
}
