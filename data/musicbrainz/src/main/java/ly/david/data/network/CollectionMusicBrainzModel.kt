package ly.david.data.network

import com.squareup.moshi.Json

data class CollectionMusicBrainzModel(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "entity-type") val entity: MusicBrainzResource,
    @Json(name = "type") val type: String? = null,
    @Json(name = "type-id") val typeId: String? = null,
    @Json(name = "editor") val editor: String,

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
)

fun CollectionMusicBrainzModel.getCount(): Int {
    return when (entity) {
        MusicBrainzResource.AREA -> areaCount
        MusicBrainzResource.ARTIST -> artistCount
        MusicBrainzResource.EVENT -> eventCount
        MusicBrainzResource.INSTRUMENT -> instrumentCount
        MusicBrainzResource.LABEL -> labelCount
        MusicBrainzResource.PLACE -> placeCount
        MusicBrainzResource.RECORDING -> recordingCount
        MusicBrainzResource.RELEASE -> releaseCount
        MusicBrainzResource.RELEASE_GROUP -> releaseGroupCount
        MusicBrainzResource.SERIES -> seriesCount
        MusicBrainzResource.WORK -> workCount
        else -> 0
    } ?: 0
}
