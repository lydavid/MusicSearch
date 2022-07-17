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

// Find the rest here: https://musicbrainz.org/relationships
private val relationshipLabels = hashMapOf(

    // https://musicbrainz.org/relationships/area-area
    "de7cc874-8b1b-3a05-8272-f3834c968fb7" to ("parts" to "part of"),

    // https://musicbrainz.org/relationships/area-event
    // https://musicbrainz.org/relationships/area-recording
    // https://musicbrainz.org/relationships/area-release
    // https://musicbrainz.org/relationships/area-series
    // https://musicbrainz.org/relationships/area-work
    // TODO:

    // https://musicbrainz.org/relationships/area-genre
    "25ed73f8-a864-42cf-8b9c-68db198dbe0e" to ("genres" to "from"),

    // https://musicbrainz.org/relationships/area-instrument
    "0b67183b-9f36-4b09-b561-0fa531508f91" to ("instruments" to "from"),

    // https://musicbrainz.org/relationships/area-url

    // https://musicbrainz.org/relationships/artist-instrument
    "87bfa63d-c91f-4bf2-9051-5103f7d181dd" to ("invented" to "invented by"),

    // https://musicbrainz.org/relationships/genre-instrument
    "0b4d32c8-bdba-4842-a6b5-35b2ca2f4f11" to ("used instruments" to "used in"),

    // https://musicbrainz.org/relationships/instrument-label
    "9a1365db-5cce-4be6-9a6c-fbf566b26913" to ("invented by" to "invented"),

    // https://musicbrainz.org/relationships/instrument-instrument
    "12678b88-1adb-3536-890e-9b39b9a14b2d" to ("children" to "child of"),
    "deaf1d50-e624-3069-91bd-88e51cafd605" to ("derivations" to "derived from"),
    "5ee4568f-d8bd-321d-9426-0ff6819ae6b5" to ("consists of" to "part of"),
    "0fd327f5-8be4-3b9a-8852-2982c1a830ee" to ("related instruments" to "related instruments"),
    "2f522cbc-46f9-409b-9957-d0308d0899ef" to ("hybrid of" to "has hybrids"),
    "40b2bd3f-1457-3ceb-810d-57f87f0f74f0" to ("subtypes" to "type of"),
)

/**
 * Returns a human-readable label for this relationship based on its type and link direction.
 */
internal fun RelationMusicBrainzModel.getLabel(): String {

    val labels: Pair<String, String>? = relationshipLabels[typeId]

    return if (labels == null) {
        type
    } else {
        if (direction == Direction.FORWARD) labels.first else labels.second
    }
}
