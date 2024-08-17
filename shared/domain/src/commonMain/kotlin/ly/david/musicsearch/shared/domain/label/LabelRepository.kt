package ly.david.musicsearch.shared.domain.label

interface LabelRepository {
    suspend fun lookupLabel(labelId: String): LabelDetailsModel
}
