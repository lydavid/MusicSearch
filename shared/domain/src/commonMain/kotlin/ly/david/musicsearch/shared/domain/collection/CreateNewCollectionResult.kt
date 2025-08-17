package ly.david.musicsearch.shared.domain.collection

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

sealed interface CreateNewCollectionResult {
    data class NewCollection(
        val id: String? = null,
        val name: String,
        val entity: MusicBrainzEntityType,
    ) : CreateNewCollectionResult

    data object Dismissed : CreateNewCollectionResult
}
