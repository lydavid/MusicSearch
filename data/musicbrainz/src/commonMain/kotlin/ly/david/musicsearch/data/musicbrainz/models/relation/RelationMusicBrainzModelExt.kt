package ly.david.musicsearch.data.musicbrainz.models.relation

import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty

/**
 * Some [RelationMusicBrainzModel.attributes] such as "strings" does not have
 * a corresponding [RelationMusicBrainzModel.attributeValues].
 * Others such as "task" does. List these with their corresponding values.
 *
 * All attributes/attribute-value pairs should be comma-separated.
 */
fun RelationMusicBrainzModel.getFormattedAttributes(): String {
    return attributes?.joinToString(", ") { attribute ->
        attribute + attributeValues?.get(attribute).transformThisIfNotNullOrEmpty { ": $it" }
    }.orEmpty()
}

fun RelationMusicBrainzModel.getFormattedAttributeIds(): String {
    return attributes?.joinToString(", ") { attribute ->
        attributeIds?.get(attribute).orEmpty()
    }.orEmpty()
}

/**
 * Returns a human-readable label for this relationship based on its type and link direction.
 */
fun RelationMusicBrainzModel.getHeader(): String {
    val labels: Pair<String, String>? = relationshipHeaders[typeId]

    return if (labels == null) {
        type
    } else {
        if (direction == Direction.FORWARD) labels.first else labels.second
    }
}
