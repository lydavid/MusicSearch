package ly.david.musicsearch.data.musicbrainz.models.relation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
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
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.LifeSpan

@Serializable
data class RelationMusicBrainzModel(
    @SerialName("type") val type: String,
    @SerialName("type-id") val typeId: String,

    @SerialName("direction") val direction: Direction,

    @SerialName("target-type") val targetType: SerializableMusicBrainzEntity? = null,

    // prefer this credit over object's name if it exists
    @SerialName("target-credit") val targetCredit: String? = null,

    @SerialName("attributes") val attributes: List<String>? = null, // strings, task
    @SerialName("attribute-ids") val attributeIds: Map<String, String>? = null,
    @SerialName("attribute-values") val attributeValues: Map<String, String>? = null, // "director & organizer"

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
