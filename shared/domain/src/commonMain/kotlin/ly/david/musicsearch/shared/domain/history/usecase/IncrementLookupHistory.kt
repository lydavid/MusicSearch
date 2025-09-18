package ly.david.musicsearch.shared.domain.history.usecase

import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository

interface IncrementLookupHistory {
    operator fun invoke(
        oldId: String,
        lookupHistory: LookupHistory,
    )
}

class IncrementLookupHistoryImpl(
    private val lookupHistoryRepository: LookupHistoryRepository,
) : IncrementLookupHistory {
    override operator fun invoke(
        oldId: String,
        lookupHistory: LookupHistory,
    ) {
        lookupHistoryRepository.upsert(
            oldId = oldId,
            lookupHistory = lookupHistory,
        )
    }
}
