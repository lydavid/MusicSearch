package ly.david.musicsearch.shared.domain.work

import ly.david.musicsearch.shared.domain.details.WorkDetailsModel

interface WorkRepository {
    suspend fun lookupWork(
        workId: String,
        forceRefresh: Boolean,
    ): WorkDetailsModel
}
