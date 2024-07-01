package ly.david.musicsearch.shared.domain.work

import ly.david.musicsearch.core.models.work.WorkScaffoldModel

interface WorkRepository {
    suspend fun lookupWork(
        workId: String,
    ): WorkScaffoldModel
}
