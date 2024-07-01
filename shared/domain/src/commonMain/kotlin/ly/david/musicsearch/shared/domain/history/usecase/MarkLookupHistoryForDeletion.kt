package ly.david.musicsearch.shared.domain.history.usecase

import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository
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
