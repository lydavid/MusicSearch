package ly.david.musicsearch.data.musicbrainz.models.relation

import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty

/**
 * Some [RelationMusicBrainzModel.attributes] such as "strings" does not have
 * a corresponding [RelationMusicBrainzModel.attributeValues].
 * Others such as "task" does. List these with their corresponding values.
 *
 * All attributes/attribute-value pairs should be comma-separated.
 */
fun RelationMusicBrainzModel.getFormattedAttributesForDisplay(): String =
    attributes?.joinToString(", ") { attribute ->
        when (attribute) {
            "task" -> {
                attribute + attributeValues?.task.transformThisIfNotNullOrEmpty { ": $it" }
            }
            "number" -> {
                attribute + attributeValues?.number.transformThisIfNotNullOrEmpty { ": $it" }
            }
            "time" -> {
                attribute + attributeValues?.time.transformThisIfNotNullOrEmpty { ": $it" }
            }
            else -> {
                attribute
            }
        }
    }.orEmpty()

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
