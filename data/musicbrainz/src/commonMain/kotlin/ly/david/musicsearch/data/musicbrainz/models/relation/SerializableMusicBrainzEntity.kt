package ly.david.musicsearch.data.musicbrainz.models.relation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

@Serializable
enum class SerializableMusicBrainzEntity(val entity: MusicBrainzEntityType) {
    @SerialName("area")
    AREA(MusicBrainzEntityType.AREA),

    @SerialName("artist")
    ARTIST(MusicBrainzEntityType.ARTIST),

    @SerialName("event")
    EVENT(MusicBrainzEntityType.EVENT),

    @SerialName("genre")
    GENRE(MusicBrainzEntityType.GENRE),

    @SerialName("instrument")
    INSTRUMENT(MusicBrainzEntityType.INSTRUMENT),

    @SerialName("label")
    LABEL(MusicBrainzEntityType.LABEL),

    @SerialName("place")
    PLACE(MusicBrainzEntityType.PLACE),

    @SerialName("recording")
    RECORDING(MusicBrainzEntityType.RECORDING),

    @SerialName("release")
    RELEASE(MusicBrainzEntityType.RELEASE),

    // Note that target-type uses release_group, while uri uses release-group.
    // For our domain entity, we will always prefer release-group.
    @SerialName("release_group")
    RELEASE_GROUP(MusicBrainzEntityType.RELEASE_GROUP),

    @SerialName("series")
    SERIES(MusicBrainzEntityType.SERIES),

    @SerialName("work")
    WORK(MusicBrainzEntityType.WORK),

    @SerialName("url")
    URL(MusicBrainzEntityType.URL),
}
