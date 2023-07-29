package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.ILifeSpan

data class RelationMusicBrainzModel(

    // As long as type is unique, we can match its name, rather than type-id, since its name is more accessible to us
    //  though we have to search up its forward/reverse link phrase anyways, which gives us its type-id.
    //  Maybe we should just use that for future proofing.
    @Json(name = "type") val type: String,
    @Json(name = "type-id") val typeId: String,

    // TODO: we should order with backward relations first
    @Json(name = "direction") val direction: Direction,

    @Json(name = "target-type") val targetType: MusicBrainzEntity?,

    // prefer this credit over object's name if it exists
    @Json(name = "target-credit") val targetCredit: String? = null,

    @Json(name = "attributes") val attributes: List<String>? = null, // strings, task
    @Json(name = "attribute-values") val attributeValues: AttributeValue? = null, // "director & organizer"

    @Json(name = "begin") override val begin: String? = null,
    @Json(name = "end") override val end: String? = null,
    @Json(name = "ended") override val ended: Boolean? = null,

    @Json(name = "area") val area: AreaMusicBrainzModel? = null,
    @Json(name = "artist") val artist: ArtistMusicBrainzModel? = null, // could be composer, arranger, etc
    @Json(name = "event") val event: EventMusicBrainzModel? = null,
    @Json(name = "genre") val genre: GenreMusicBrainzModel? = null,
    @Json(name = "instrument") val instrument: InstrumentMusicBrainzModel? = null,
    @Json(name = "label") val label: LabelMusicBrainzModel? = null,
    @Json(name = "place") val place: PlaceMusicBrainzModel? = null,
    @Json(name = "recording") val recording: RecordingMusicBrainzModel? = null,
    @Json(name = "release") val release: ReleaseMusicBrainzModel? = null,
    @Json(name = "release_group") val releaseGroup: ReleaseGroupMusicBrainzModel? = null,
    @Json(name = "series") val series: SeriesMusicBrainzModel? = null,
    @Json(name = "work") val work: WorkMusicBrainzModel? = null,
    @Json(name = "url") val url: UrlMusicBrainzModel? = null,
) : ILifeSpan

enum class Direction {
    @Json(name = "backward")
    BACKWARD,

    @Json(name = "forward")
    FORWARD,
}

// TODO: instead of this, just use Map<String, String>
// TODO: add the rest of relationship attributes
//  there isn't actually that many of them
//  https://musicbrainz.org/relationship-attributes
data class AttributeValue(

    /**
     * https://musicbrainz.org/relationship/68330a36-44cf-4fa2-84e8-533c6fe3fc23
     */
    @Json(name = "task")
    val task: String? = null,

    @Json(name = "number")
    val number: String? = null,
)
