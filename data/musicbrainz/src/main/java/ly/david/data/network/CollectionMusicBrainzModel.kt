package ly.david.data.network

import com.squareup.moshi.Json

data class CollectionMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "name") override val name: String,
    override val disambiguation: String? = null, // TODO: unused, is MusicBrainzModel too strict?
    @Json(name = "entity-type") val entity: MusicBrainzEntity,
    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "editor") val editor: String = "",

    @Json(name = "area-count") val areaCount: Int? = null,
    @Json(name = "artist-count") val artistCount: Int? = null,
    @Json(name = "event-count") val eventCount: Int? = null,
    @Json(name = "instrument-count") val instrumentCount: Int? = null,
    @Json(name = "label-count") val labelCount: Int? = null,
    @Json(name = "place-count") val placeCount: Int? = null,
    @Json(name = "recording-count") val recordingCount: Int? = null,
    @Json(name = "release-count") val releaseCount: Int? = null,
    @Json(name = "release-group-count") val releaseGroupCount: Int? = null,
    @Json(name = "series-count") val seriesCount: Int? = null,
    @Json(name = "work-count") val workCount: Int? = null,
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
