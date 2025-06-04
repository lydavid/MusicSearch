package ly.david.musicsearch.data.musicbrainz.models.relation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.LifeSpan
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel

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

    @SerialName("area") val area: AreaMusicBrainzNetworkModel? = null,
    @SerialName("artist") val artist: ArtistMusicBrainzNetworkModel? = null, // could be composer, arranger, etc
    @SerialName("event") val event: EventMusicBrainzNetworkModel? = null,
    @SerialName("genre") val genre: GenreMusicBrainzNetworkModel? = null,
    @SerialName("instrument") val instrument: InstrumentMusicBrainzNetworkModel? = null,
    @SerialName("label") val label: LabelMusicBrainzNetworkModel? = null,
    @SerialName("place") val place: PlaceMusicBrainzNetworkModel? = null,
    @SerialName("recording") val recording: RecordingMusicBrainzNetworkModel? = null,
    @SerialName("release") val release: ReleaseMusicBrainzNetworkModel? = null,
    @SerialName("release_group") val releaseGroup: ReleaseGroupMusicBrainzNetworkModel? = null,
    @SerialName("series") val series: SeriesMusicBrainzNetworkModel? = null,
    @SerialName("work") val work: WorkMusicBrainzNetworkModel? = null,
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
    @SerialName("time") val time: String? = null,
)
