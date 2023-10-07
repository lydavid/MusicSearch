package ly.david.musicsearch.domain.history

import org.koin.core.annotation.Single

@Single
class UnMarkLookupHistoryForDeletion(
    private val lookupHistoryRepository: LookupHistoryRepository,
) {
    operator fun invoke(mbid: String = "") {
        if (mbid.isEmpty()) {
            lookupHistoryRepository.undoDeleteAllHistory()
        } else {
            lookupHistoryRepository.undoDeleteHistory(mbid)
        }
    }
}
