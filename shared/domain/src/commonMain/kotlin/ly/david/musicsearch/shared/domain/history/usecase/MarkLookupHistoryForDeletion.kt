package ly.david.musicsearch.shared.domain.history.usecase

import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository

interface MarkLookupHistoryForDeletion {
    operator fun invoke(mbid: String = "")
}

class MarkLookupHistoryForDeletionImpl(
    private val lookupHistoryRepository: LookupHistoryRepository,
) : MarkLookupHistoryForDeletion {
    override operator fun invoke(mbid: String) {
        if (mbid.isEmpty()) {
            lookupHistoryRepository.markAllHistoryAsDeleted()
        } else {
            lookupHistoryRepository.markHistoryAsDeleted(mbid)
        }
    }
}
