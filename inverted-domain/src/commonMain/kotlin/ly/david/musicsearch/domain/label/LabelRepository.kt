package ly.david.musicsearch.domain.label

import ly.david.musicsearch.core.models.label.LabelScaffoldModel

interface LabelRepository {
    suspend fun lookupLabel(labelId: String): LabelScaffoldModel
}
