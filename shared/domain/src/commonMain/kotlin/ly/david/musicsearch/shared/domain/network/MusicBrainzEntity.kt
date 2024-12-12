package ly.david.musicsearch.shared.domain.network

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

enum class MusicBrainzEntity {
    AREA,

    ARTIST,

    // Non-core
    COLLECTION,

    EVENT,

    // Not searchable, but lookupable
    GENRE,

    INSTRUMENT,

    LABEL,

    PLACE,

    RECORDING,

    RELEASE,

    RELEASE_GROUP,

    SERIES,

    WORK,

    // Not searchable, but lookupable
    URL,

    // Other searchable: annotation, tag, cd stub, editor, documentation

    // Other non-core entities: rating, tag

    // Other lookupable: discid, isrc, iswc
}

val searchableEntities: List<MusicBrainzEntity>
    get() = listOf(
        MusicBrainzEntity.AREA,
        MusicBrainzEntity.ARTIST,
        MusicBrainzEntity.EVENT,
        MusicBrainzEntity.INSTRUMENT,
        MusicBrainzEntity.LABEL,
        MusicBrainzEntity.PLACE,
        MusicBrainzEntity.RECORDING,
        MusicBrainzEntity.RELEASE,
        MusicBrainzEntity.RELEASE_GROUP,
        MusicBrainzEntity.SERIES,
        MusicBrainzEntity.WORK,
    )

val collectableEntities: List<MusicBrainzEntity>
    get() = listOf(
        MusicBrainzEntity.AREA,
        MusicBrainzEntity.ARTIST,
        MusicBrainzEntity.EVENT,
        MusicBrainzEntity.INSTRUMENT,
        MusicBrainzEntity.LABEL,
        MusicBrainzEntity.PLACE,
        MusicBrainzEntity.RECORDING,
        MusicBrainzEntity.RELEASE,
        MusicBrainzEntity.RELEASE_GROUP,
        MusicBrainzEntity.SERIES,
        MusicBrainzEntity.WORK,
    )

val relatableEntities: List<MusicBrainzEntity>
    get() = listOf(
        MusicBrainzEntity.AREA,
        MusicBrainzEntity.ARTIST,
        MusicBrainzEntity.EVENT,
        MusicBrainzEntity.GENRE,
        MusicBrainzEntity.INSTRUMENT,
        MusicBrainzEntity.LABEL,
        MusicBrainzEntity.PLACE,
        MusicBrainzEntity.RECORDING,
        MusicBrainzEntity.RELEASE,
        MusicBrainzEntity.RELEASE_GROUP,
        MusicBrainzEntity.SERIES,
        MusicBrainzEntity.WORK,
        MusicBrainzEntity.URL,
    )

fun String.toMusicBrainzEntity(): MusicBrainzEntity? =
    MusicBrainzEntity.entries.firstOrNull { this == it.resourceUri }

/**
 * The uri resource to use for query/lookup request to MusicBrainz.
 * And for deeplinking.
 */
val MusicBrainzEntity.resourceUri: String
    get() = when (this) {
        MusicBrainzEntity.AREA -> RESOURCE_AREA
        MusicBrainzEntity.ARTIST -> RESOURCE_ARTIST
        MusicBrainzEntity.COLLECTION -> RESOURCE_COLLECTION
        MusicBrainzEntity.EVENT -> RESOURCE_EVENT
        MusicBrainzEntity.GENRE -> RESOURCE_GENRE
        MusicBrainzEntity.INSTRUMENT -> RESOURCE_INSTRUMENT
        MusicBrainzEntity.LABEL -> RESOURCE_LABEL
        MusicBrainzEntity.PLACE -> RESOURCE_PLACE
        MusicBrainzEntity.RECORDING -> RESOURCE_RECORDING
        MusicBrainzEntity.RELEASE -> RESOURCE_RELEASE
        MusicBrainzEntity.RELEASE_GROUP -> RESOURCE_RELEASE_GROUP
        MusicBrainzEntity.SERIES -> RESOURCE_SERIES
        MusicBrainzEntity.WORK -> RESOURCE_WORK
        MusicBrainzEntity.URL -> RESOURCE_URL
    }

/**
 * The plural uri resource to use for PUT/DELETE collection requests to MusicBrainz.
 */
val MusicBrainzEntity.resourceUriPlural: String
    get() = when (this) {
        MusicBrainzEntity.AREA -> PLURAL_RESOURCE_AREA
        MusicBrainzEntity.ARTIST -> PLURAL_RESOURCE_ARTIST
        MusicBrainzEntity.COLLECTION -> PLURAL_RESOURCE_COLLECTION
        MusicBrainzEntity.EVENT -> PLURAL_RESOURCE_EVENT
        MusicBrainzEntity.GENRE -> PLURAL_RESOURCE_GENRE
        MusicBrainzEntity.INSTRUMENT -> PLURAL_RESOURCE_INSTRUMENT
        MusicBrainzEntity.LABEL -> PLURAL_RESOURCE_LABEL
        MusicBrainzEntity.PLACE -> PLURAL_RESOURCE_PLACE
        MusicBrainzEntity.RECORDING -> PLURAL_RESOURCE_RECORDING
        MusicBrainzEntity.RELEASE -> PLURAL_RESOURCE_RELEASE
        MusicBrainzEntity.RELEASE_GROUP -> PLURAL_RESOURCE_RELEASE_GROUP
        MusicBrainzEntity.SERIES -> PLURAL_RESOURCE_SERIES
        MusicBrainzEntity.WORK -> PLURAL_RESOURCE_WORK
        MusicBrainzEntity.URL -> PLURAL_RESOURCE_URL
    }
