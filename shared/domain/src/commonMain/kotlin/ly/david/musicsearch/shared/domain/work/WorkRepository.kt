package ly.david.musicsearch.shared.domain.work

import ly.david.musicsearch.shared.domain.work.WorkDetailsModel

interface WorkRepository {
    suspend fun lookupWork(
        workId: String,
    ): WorkDetailsModel
}
