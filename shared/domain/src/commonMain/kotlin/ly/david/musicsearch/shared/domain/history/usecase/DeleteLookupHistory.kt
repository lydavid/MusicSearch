package ly.david.musicsearch.shared.domain.history.usecase

import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository

class DeleteLookupHistory(
    private val lookupHistoryRepository: LookupHistoryRepository,
) {
    operator fun invoke(mbid: String = "") {
        if (mbid.isEmpty()) {
            lookupHistoryRepository.deleteAllHistory()
        } else {
            lookupHistoryRepository.deleteHistory(mbid)
        }
    }
}
