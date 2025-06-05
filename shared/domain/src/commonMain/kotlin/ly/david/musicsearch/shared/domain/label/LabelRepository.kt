package ly.david.musicsearch.shared.domain.label

import ly.david.musicsearch.shared.domain.details.LabelDetailsModel

interface LabelRepository {
    suspend fun lookupLabel(
        labelId: String,
        forceRefresh: Boolean,
    ): LabelDetailsModel
}
