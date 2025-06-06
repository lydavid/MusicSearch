package ly.david.musicsearch.shared.domain.label

import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel

interface LabelRepository {
    suspend fun lookupLabel(
        labelId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): LabelDetailsModel
}
