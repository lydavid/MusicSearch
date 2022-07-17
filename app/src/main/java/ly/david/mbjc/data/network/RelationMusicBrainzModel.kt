package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.ILifeSpan
import ly.david.mbjc.data.LabelMusicBrainzModel
import ly.david.mbjc.data.WorkMusicBrainzModel
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

internal data class RelationMusicBrainzModel(

    // As long as type is unique, we can match its name, rather than type-id, since its name is more accessible to us
    //  though we have to search up its forward/reverse link phrase anyways, which gives us its type-id.
    //  Maybe we should just use that for future proofing.
    @Json(name = "type") val type: String,
    @Json(name = "type-id") val typeId: String,
    @Json(name = "direction") val direction: Direction,

    @Json(name = "target-type") val targetType: MusicBrainzResource, // artist, place, work, label
    @Json(name = "target-credit") val targetCredit: String? = null, // prefer this credit over object's name if it exists

    @Json(name = "attributes") val attributes: List<String>? = null, // strings, task
    @Json(name = "attribute-values") val attributeValues: AttributeValue? = null, // "director & organizer"

    @Json(name = "begin") override val begin: String?,
    @Json(name = "end") override val end: String?,
    @Json(name = "ended") override val ended: Boolean?,

    @Json(name = "artist") val artist: ArtistMusicBrainzModel? = null, // could be composer, arranger, etc
    @Json(name = "label") val label: LabelMusicBrainzModel? = null,
    @Json(name = "work") val work: WorkMusicBrainzModel? = null,
    @Json(name = "area") val area: AreaMusicBrainzModel? = null,
    @Json(name = "place") val place: PlaceMusicBrainzModel? = null,
    @Json(name = "url") val url: UrlMusicBrainzModel? = null,
    @Json(name = "recording") val recording: RecordingMusicBrainzModel? = null,
    @Json(name = "instrument") val instrument: InstrumentMusicBrainzModel? = null,

    @Json(name = "genre") val genre: GenreMusicBrainzModel? = null,
) : ILifeSpan

internal enum class Direction {
    @Json(name = "backward") BACKWARD,
    @Json(name = "forward") FORWARD
}

// TODO: rest of attributes that have a corresponding value
//  would be nice if we didn't need a field for each one, seeing as how they all have a string value...
internal data class AttributeValue(

    /**
     * https://musicbrainz.org/relationship/68330a36-44cf-4fa2-84e8-533c6fe3fc23
     */
    @Json(name = "task")
    val task: String? = null
)

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

// is there a better way than hardcoding all of these?
//  probably not unless we have access to an api that exposes all of these for us
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
