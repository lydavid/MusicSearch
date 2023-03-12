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

private const val PLURAL_RESOURCE_AREA = "areas"
private const val PLURAL_RESOURCE_ARTIST = "artists"
private const val PLURAL_RESOURCE_COLLECTION = "collections"
private const val PLURAL_RESOURCE_EVENT = "events"
private const val PLURAL_RESOURCE_GENRE = "genres"
private const val PLURAL_RESOURCE_INSTRUMENT = "instruments"
private const val PLURAL_RESOURCE_LABEL = "labels"
private const val PLURAL_RESOURCE_PLACE = "places"
private const val PLURAL_RESOURCE_RECORDING = "recordings"
private const val PLURAL_RESOURCE_RELEASE = "releases"
private const val PLURAL_RESOURCE_RELEASE_GROUP = "release-groups"
private const val PLURAL_RESOURCE_SERIES = "series"
private const val PLURAL_RESOURCE_WORK = "works"
private const val PLURAL_RESOURCE_URL = "urls"

/**
 * These are resources available for lookup requests. Many of them are query-able as well.
 */
enum class MusicBrainzResource {
    @Json(name = "area")
    AREA,

    @Json(name = "artist")
    ARTIST,

    // Non-core
    @Json(name = "collection")
    COLLECTION,

    @Json(name = "event")
    EVENT,

    // Not searchable, but lookupable
    @Json(name = "genre")
    GENRE,

    @Json(name = "instrument")
    INSTRUMENT,

    @Json(name = "label")
    LABEL,

    @Json(name = "place")
    PLACE,

    @Json(name = "recording")
    RECORDING,

    @Json(name = "release")
    RELEASE,

    // Note that target-type uses release_group, while uri uses release-group.
    // For our internal resource, we will use release-group.
    @Json(name = "release_group")
    RELEASE_GROUP,

    @Json(name = "series")
    SERIES,

    @Json(name = "work")
    WORK,

    // Not searchable, but lookupable
    @Json(name = "url")
    URL,

    // Other searchable: annotation, tag, cd stub, editor, documentation

    // Other non-core resources with API: rating, tag

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
    MusicBrainzResource.values().firstOrNull { this == it.resourceUri }

/**
 * The uri resource to use for query/lookup request to MusicBrainz.
 * And for deeplinking.
 */
val MusicBrainzResource.resourceUri: String
    get() = when (this) {
        MusicBrainzResource.AREA -> RESOURCE_AREA
        MusicBrainzResource.ARTIST -> RESOURCE_ARTIST
        MusicBrainzResource.COLLECTION -> RESOURCE_COLLECTION
        MusicBrainzResource.EVENT -> RESOURCE_EVENT
        MusicBrainzResource.GENRE -> RESOURCE_GENRE
        MusicBrainzResource.INSTRUMENT -> RESOURCE_INSTRUMENT
        MusicBrainzResource.LABEL -> RESOURCE_LABEL
        MusicBrainzResource.PLACE -> RESOURCE_PLACE
        MusicBrainzResource.RECORDING -> RESOURCE_COLLECTION
        MusicBrainzResource.RELEASE -> RESOURCE_RELEASE
        MusicBrainzResource.RELEASE_GROUP -> RESOURCE_RELEASE_GROUP
        MusicBrainzResource.SERIES -> RESOURCE_SERIES
        MusicBrainzResource.WORK -> RESOURCE_WORK
        MusicBrainzResource.URL -> RESOURCE_URL
    }

/**
 * The plural uri resource to use for PUT/DELETE collection requests to MusicBrainz.
 */
val MusicBrainzResource.resourceUriPlural: String
    get() = when (this) {
        MusicBrainzResource.AREA -> PLURAL_RESOURCE_AREA
        MusicBrainzResource.ARTIST -> PLURAL_RESOURCE_ARTIST
        MusicBrainzResource.COLLECTION -> PLURAL_RESOURCE_COLLECTION
        MusicBrainzResource.EVENT -> PLURAL_RESOURCE_EVENT
        MusicBrainzResource.GENRE -> PLURAL_RESOURCE_GENRE
        MusicBrainzResource.INSTRUMENT -> PLURAL_RESOURCE_INSTRUMENT
        MusicBrainzResource.LABEL -> PLURAL_RESOURCE_LABEL
        MusicBrainzResource.PLACE -> PLURAL_RESOURCE_PLACE
        MusicBrainzResource.RECORDING -> PLURAL_RESOURCE_COLLECTION
        MusicBrainzResource.RELEASE -> PLURAL_RESOURCE_RELEASE
        MusicBrainzResource.RELEASE_GROUP -> PLURAL_RESOURCE_RELEASE_GROUP
        MusicBrainzResource.SERIES -> PLURAL_RESOURCE_SERIES
        MusicBrainzResource.WORK -> PLURAL_RESOURCE_WORK
        MusicBrainzResource.URL -> PLURAL_RESOURCE_URL
    }
