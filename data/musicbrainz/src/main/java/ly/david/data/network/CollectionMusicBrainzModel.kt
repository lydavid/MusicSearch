package ly.david.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionMusicBrainzModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String,
    override val disambiguation: String? = null, // TODO: unused, is MusicBrainzModel too strict?
    @SerialName("entity-type") val entity: MusicBrainzEntity,
    @SerialName("type") val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("editor") val editor: String = "",

    @SerialName("area-count") val areaCount: Int? = null,
    @SerialName("artist-count") val artistCount: Int? = null,
    @SerialName("event-count") val eventCount: Int? = null,
    @SerialName("instrument-count") val instrumentCount: Int? = null,
    @SerialName("label-count") val labelCount: Int? = null,
    @SerialName("place-count") val placeCount: Int? = null,
    @SerialName("recording-count") val recordingCount: Int? = null,
    @SerialName("release-count") val releaseCount: Int? = null,
    @SerialName("release-group-count") val releaseGroupCount: Int? = null,
    @SerialName("series-count") val seriesCount: Int? = null,
    @SerialName("work-count") val workCount: Int? = null,
) : MusicBrainzModel()

fun CollectionMusicBrainzModel.getCount(): Int {
    return when (entity) {
        MusicBrainzEntity.AREA -> areaCount
        MusicBrainzEntity.ARTIST -> artistCount
        MusicBrainzEntity.EVENT -> eventCount
        MusicBrainzEntity.INSTRUMENT -> instrumentCount
        MusicBrainzEntity.LABEL -> labelCount
        MusicBrainzEntity.PLACE -> placeCount
        MusicBrainzEntity.RECORDING -> recordingCount
        MusicBrainzEntity.RELEASE -> releaseCount
        MusicBrainzEntity.RELEASE_GROUP -> releaseGroupCount
        MusicBrainzEntity.SERIES -> seriesCount
        MusicBrainzEntity.WORK -> workCount
        else -> 0
    } ?: 0
}
