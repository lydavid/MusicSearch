package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.LabelMusicBrainzModel
import ly.david.mbjc.data.WorkMusicBrainzModel

internal data class RelationMusicBrainzModel(
    @Json(name = "type")
    val type: String,
    @Json(name = "target-type")
    val targetType: MusicBrainzResource, // artist, place, work, label
    @Json(name = "target-credit")
    val targetCredit: String? = null, // prefer this credit over object's name if it exists

    @Json(name = "attributes")
    val attributes: List<String>? = null, // strings, task
    @Json(name = "attribute-values")
    val attributeValues: List<AttributeValue>? = null, // "director & organizer"

    @Json(name = "artist")
    val artist: ArtistMusicBrainzModel? = null, // could be composer, arranger, etc
    @Json(name = "label")
    val label: LabelMusicBrainzModel? = null,
    @Json(name = "work")
    val work: WorkMusicBrainzModel? = null,
    @Json(name = "place")
    val place: PlaceMusicBrainzModel? = null,
)

// TODO: this is a very bad structure for json...
//  given a string in attributes, we need to check for that field in this data class
//  will need to map every possible value
internal data class AttributeValue(
    @Json(name = "task")
    val task: String? = null
)
