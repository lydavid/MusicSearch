package ly.david.musicsearch.domain.label

import ly.david.musicsearch.data.core.label.LabelScaffoldModel

interface LabelRepository {
    suspend fun lookupLabel(labelId: String): LabelScaffoldModel
}
