package ly.david.musicsearch.shared.domain.history.usecase

import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository

interface DeleteLookupHistory {
    operator fun invoke(mbid: String = "")
}

class DeleteLookupHistoryImpl(
    private val lookupHistoryRepository: LookupHistoryRepository,
) : DeleteLookupHistory {
    override operator fun invoke(mbid: String) {
        if (mbid.isEmpty()) {
            lookupHistoryRepository.deleteAllHistory()
        } else {
            lookupHistoryRepository.deleteHistory(mbid)
        }
    }
}
