package ly.david.musicsearch.data.musicbrainz.models.relation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.LifeSpan
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel

@Serializable
data class RelationMusicBrainzModel(

    // As long as type is unique, we can match its name, rather than type-id, since its name is more accessible to us
    //  though we have to search up its forward/reverse link phrase anyways, which gives us its type-id.
    //  Maybe we should just use that for future proofing.
    @SerialName("type") val type: String,
    @SerialName("type-id") val typeId: String,

    @SerialName("direction") val direction: Direction,

    @SerialName("target-type") val targetType: SerializableMusicBrainzEntity? = null,

    // prefer this credit over object's name if it exists
    @SerialName("target-credit") val targetCredit: String? = null,

    @SerialName("attributes") val attributes: List<String>? = null, // strings, task
    @SerialName("attribute-values") val attributeValues: AttributeValue? = null, // "director & organizer"

    @SerialName("begin") override val begin: String? = null,
    @SerialName("end") override val end: String? = null,
    @SerialName("ended") override val ended: Boolean? = null,

    @SerialName("ordering-key") val orderingKey: Int? = null,

    @SerialName("area") val area: AreaMusicBrainzModel? = null,
    @SerialName("artist") val artist: ArtistMusicBrainzModel? = null, // could be composer, arranger, etc
    @SerialName("event") val event: EventMusicBrainzModel? = null,
    @SerialName("genre") val genre: GenreMusicBrainzModel? = null,
    @SerialName("instrument") val instrument: InstrumentMusicBrainzModel? = null,
    @SerialName("label") val label: LabelMusicBrainzModel? = null,
    @SerialName("place") val place: PlaceMusicBrainzModel? = null,
    @SerialName("recording") val recording: RecordingMusicBrainzModel? = null,
    @SerialName("release") val release: ReleaseMusicBrainzModel? = null,
    @SerialName("release_group") val releaseGroup: ReleaseGroupMusicBrainzModel? = null,
    @SerialName("series") val series: SeriesMusicBrainzModel? = null,
    @SerialName("work") val work: WorkMusicBrainzModel? = null,
    @SerialName("url") val url: UrlMusicBrainzModel? = null,
) : LifeSpan

enum class Direction {
    @SerialName("backward")
    BACKWARD,

    @SerialName("forward")
    FORWARD,
}

// TODO: use Map<String, String> or add the rest of relationship attributes
//  there isn't actually that many of them
//  https://musicbrainz.org/relationship-attributes
@Serializable
data class AttributeValue(
    @SerialName("task") val task: String? = null,
    @SerialName("number") val number: String? = null,
)
