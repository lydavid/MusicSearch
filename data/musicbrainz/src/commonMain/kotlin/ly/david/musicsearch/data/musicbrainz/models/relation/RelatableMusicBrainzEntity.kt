package ly.david.musicsearch.data.musicbrainz.models.relation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

@Serializable
enum class RelatableMusicBrainzEntity(val entity: MusicBrainzEntity) {
    @SerialName("area")
    AREA(MusicBrainzEntity.AREA),

    @SerialName("artist")
    ARTIST(MusicBrainzEntity.ARTIST),

    @SerialName("event")
    EVENT(MusicBrainzEntity.EVENT),

    @SerialName("genre")
    GENRE(MusicBrainzEntity.GENRE),

    @SerialName("instrument")
    INSTRUMENT(MusicBrainzEntity.INSTRUMENT),

    @SerialName("label")
    LABEL(MusicBrainzEntity.LABEL),

    @SerialName("place")
    PLACE(MusicBrainzEntity.PLACE),

    @SerialName("recording")
    RECORDING(MusicBrainzEntity.RECORDING),

    @SerialName("release")
    RELEASE(MusicBrainzEntity.RELEASE),

    // Note that target-type uses release_group, while uri uses release-group.
    // For our domain entity, we will always prefer release-group.
    @SerialName("release_group")
    RELEASE_GROUP(MusicBrainzEntity.RELEASE_GROUP),

    @SerialName("series")
    SERIES(MusicBrainzEntity.SERIES),

    @SerialName("work")
    WORK(MusicBrainzEntity.WORK),

    @SerialName("url")
    URL(MusicBrainzEntity.URL),
}
