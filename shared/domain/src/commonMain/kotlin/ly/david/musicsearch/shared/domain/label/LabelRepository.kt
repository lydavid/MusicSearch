package ly.david.musicsearch.shared.domain.label

import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import kotlin.time.Instant

interface LabelRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): LabelDetailsModel
}
