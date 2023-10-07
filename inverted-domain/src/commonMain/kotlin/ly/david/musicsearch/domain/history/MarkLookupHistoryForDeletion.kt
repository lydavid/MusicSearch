package ly.david.musicsearch.domain.history

import org.koin.core.annotation.Single

@Single
class MarkLookupHistoryForDeletion(
    private val lookupHistoryRepository: LookupHistoryRepository,
) {
    operator fun invoke(mbid: String = "") {
        if (mbid.isEmpty()) {
            lookupHistoryRepository.markAllHistoryAsDeleted()
        } else {
            lookupHistoryRepository.markHistoryAsDeleted(mbid)
        }
    }
}
