package ly.david.data.network

import com.squareup.moshi.Json

const val RESOURCE_AREA = "area"
const val RESOURCE_ARTIST = "artist"
const val RESOURCE_COLLECTION = "collection"
const val RESOURCE_EVENT = "event"
const val RESOURCE_GENRE = "genre"
const val RESOURCE_INSTRUMENT = "instrument"
const val RESOURCE_LABEL = "label"
const val RESOURCE_PLACE = "place"
const val RESOURCE_RECORDING = "recording"
const val RESOURCE_RELEASE = "release"
const val RESOURCE_RELEASE_GROUP = "release-group"
const val RESOURCE_SERIES = "series"
const val RESOURCE_WORK = "work"
const val RESOURCE_URL = "url"

/**
 * These are resources available for lookup requests. Many of them are query-able as well.
 *
 * @param resourceName The resource to use for query/lookup request to MusicBrainz.
 */
enum class MusicBrainzResource(val resourceName: String) {
    @Json(name = "area")
    AREA(RESOURCE_AREA),

    @Json(name = "artist")
    ARTIST(RESOURCE_ARTIST),

    @Json(name = "collection")
    COLLECTION(RESOURCE_COLLECTION),

    @Json(name = "event")
    EVENT(RESOURCE_EVENT),

    // Not searchable, but lookupable
    @Json(name = "genre")
    GENRE(RESOURCE_GENRE),

    @Json(name = "instrument")
    INSTRUMENT(RESOURCE_INSTRUMENT),

    @Json(name = "label")
    LABEL(RESOURCE_LABEL),

    @Json(name = "place")
    PLACE(RESOURCE_PLACE),

    @Json(name = "recording")
    RECORDING(RESOURCE_RECORDING),

    @Json(name = "release")
    RELEASE(RESOURCE_RELEASE),

    // Note that target-type uses release_group, while uri uses release-group.
    // For our internal resource, we will use release-group.
    @Json(name = "release_group")
    RELEASE_GROUP(RESOURCE_RELEASE_GROUP),

    @Json(name = "series")
    SERIES(RESOURCE_SERIES),

    @Json(name = "work")
    WORK(RESOURCE_WORK),

    // Not searchable, but lookupable
    @Json(name = "url")
    URL(RESOURCE_URL),

    // Other searchable: annotation, tag, cd stub, editor, documentation

    // Other non-core resources with API: rating, tag, collection

    // Other lookupable: discid, isrc, iswc
}

val searchableResources: List<MusicBrainzResource>
    get() = listOf(
        MusicBrainzResource.AREA,
        MusicBrainzResource.ARTIST,
        MusicBrainzResource.EVENT,
        MusicBrainzResource.INSTRUMENT,
        MusicBrainzResource.LABEL,
        MusicBrainzResource.PLACE,
        MusicBrainzResource.RECORDING,
        MusicBrainzResource.RELEASE,
        MusicBrainzResource.RELEASE_GROUP,
        MusicBrainzResource.SERIES,
        MusicBrainzResource.WORK,
    )

val collectableResources: List<MusicBrainzResource>
    get() = listOf(
        MusicBrainzResource.AREA,
        MusicBrainzResource.ARTIST,
        MusicBrainzResource.EVENT,
        MusicBrainzResource.INSTRUMENT,
        MusicBrainzResource.LABEL,
        MusicBrainzResource.PLACE,
        MusicBrainzResource.RECORDING,
        MusicBrainzResource.RELEASE,
        MusicBrainzResource.RELEASE_GROUP,
        MusicBrainzResource.SERIES,
        MusicBrainzResource.WORK,
    )

fun String.toMusicBrainzResource(): MusicBrainzResource? =
    MusicBrainzResource.values().firstOrNull { this == it.resourceName }
