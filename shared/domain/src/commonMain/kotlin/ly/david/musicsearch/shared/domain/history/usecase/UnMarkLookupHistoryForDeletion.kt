package ly.david.musicsearch.shared.domain.history.usecase

import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository

interface UnMarkLookupHistoryForDeletion {
    operator fun invoke(mbid: String = "")
}

class UnMarkLookupHistoryForDeletionImpl(
    private val lookupHistoryRepository: LookupHistoryRepository,
) : UnMarkLookupHistoryForDeletion {
    override operator fun invoke(mbid: String) {
        if (mbid.isEmpty()) {
            lookupHistoryRepository.undoDeleteAllHistory()
        } else {
            lookupHistoryRepository.undoDeleteHistory(mbid)
        }
    }
}
