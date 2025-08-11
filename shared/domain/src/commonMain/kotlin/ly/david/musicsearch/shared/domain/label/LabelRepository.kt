package ly.david.musicsearch.shared.domain.label

import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel

interface LabelRepository {
    suspend fun lookupLabel(
        labelId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): LabelDetailsModel
}
