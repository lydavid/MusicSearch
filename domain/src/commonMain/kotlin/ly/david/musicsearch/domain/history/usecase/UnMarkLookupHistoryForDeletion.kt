package ly.david.musicsearch.domain.history.usecase

import ly.david.musicsearch.domain.history.LookupHistoryRepository
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
