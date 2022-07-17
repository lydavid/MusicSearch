package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.ILifeSpan
import ly.david.mbjc.data.LabelMusicBrainzModel
import ly.david.mbjc.data.WorkMusicBrainzModel

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
