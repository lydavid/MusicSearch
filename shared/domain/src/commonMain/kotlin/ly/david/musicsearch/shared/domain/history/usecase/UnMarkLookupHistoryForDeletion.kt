package ly.david.musicsearch.shared.domain.history.usecase

import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository

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
