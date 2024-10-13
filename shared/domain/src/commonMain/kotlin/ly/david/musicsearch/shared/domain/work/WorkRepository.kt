package ly.david.musicsearch.shared.domain.work

interface WorkRepository {
    suspend fun lookupWork(
        workId: String,
        forceRefresh: Boolean,
    ): WorkDetailsModel
}
