package ly.david.musicsearch.shared.domain.work

import ly.david.musicsearch.core.models.work.WorkDetailsModel

interface WorkRepository {
    suspend fun lookupWork(
        workId: String,
    ): WorkDetailsModel
}
