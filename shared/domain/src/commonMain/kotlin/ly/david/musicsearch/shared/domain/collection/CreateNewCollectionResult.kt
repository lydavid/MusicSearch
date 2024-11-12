package ly.david.musicsearch.shared.domain.collection

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

sealed interface CreateNewCollectionResult {
    data class NewCollection(
        val id: String? = null,
        val name: String,
        val entity: MusicBrainzEntity,
    ) : CreateNewCollectionResult

    data object Dismissed : CreateNewCollectionResult
}
