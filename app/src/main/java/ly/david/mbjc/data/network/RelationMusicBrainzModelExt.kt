package ly.david.mbjc.data.network

import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

/**
 * Some [RelationMusicBrainzModel.attributes] such as "strings" does not have
 * a corresponding [RelationMusicBrainzModel.attributeValues].
 * Others such as "task" does. List these with their corresponding values.
 *
 * All attributes/attribute-value pairs should be comma-separated.
 */
internal fun RelationMusicBrainzModel.getFormattedAttributesForDisplay(): String =
    attributes?.joinToString(", ") { attribute ->
        if (attribute == "task") {
            attribute + attributeValues?.task.transformThisIfNotNullOrEmpty { ": $it" }
        } else {
            attribute
        }
    }.orEmpty()

/**
 * Returns a human-readable label for this relationship based on its type and link direction.
 */
internal fun RelationMusicBrainzModel.getHeader(): String {

    val labels: Pair<String, String>? = relationshipHeaders[typeId]

    return if (labels == null) {
        type
    } else {
        if (direction == Direction.FORWARD) labels.first else labels.second
    }
}
