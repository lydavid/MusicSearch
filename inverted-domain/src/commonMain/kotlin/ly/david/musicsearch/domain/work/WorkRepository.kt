package ly.david.musicsearch.domain.work

import ly.david.musicsearch.data.core.work.WorkScaffoldModel

interface WorkRepository {
    suspend fun lookupWork(
        workId: String,
    ): WorkScaffoldModel
}
