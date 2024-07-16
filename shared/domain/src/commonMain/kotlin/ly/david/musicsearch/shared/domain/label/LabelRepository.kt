package ly.david.musicsearch.shared.domain.label

import ly.david.musicsearch.core.models.label.LabelDetailsModel

interface LabelRepository {
    suspend fun lookupLabel(labelId: String): LabelDetailsModel
}
