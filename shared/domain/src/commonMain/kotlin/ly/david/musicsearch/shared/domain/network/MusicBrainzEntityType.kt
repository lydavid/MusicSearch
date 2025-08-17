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

enum class MusicBrainzEntityType {
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

val searchableEntities: List<MusicBrainzEntityType>
    get() = listOf(
        MusicBrainzEntityType.AREA,
        MusicBrainzEntityType.ARTIST,
        MusicBrainzEntityType.EVENT,
        MusicBrainzEntityType.INSTRUMENT,
        MusicBrainzEntityType.LABEL,
        MusicBrainzEntityType.PLACE,
        MusicBrainzEntityType.RECORDING,
        MusicBrainzEntityType.RELEASE,
        MusicBrainzEntityType.RELEASE_GROUP,
        MusicBrainzEntityType.SERIES,
        MusicBrainzEntityType.WORK,
    )

val collectableEntities: List<MusicBrainzEntityType>
    get() = listOf(
        MusicBrainzEntityType.AREA,
        MusicBrainzEntityType.ARTIST,
        MusicBrainzEntityType.EVENT,
        MusicBrainzEntityType.INSTRUMENT,
        MusicBrainzEntityType.LABEL,
        MusicBrainzEntityType.PLACE,
        MusicBrainzEntityType.RECORDING,
        MusicBrainzEntityType.RELEASE,
        MusicBrainzEntityType.RELEASE_GROUP,
        MusicBrainzEntityType.SERIES,
        MusicBrainzEntityType.WORK,
    )

val relatableEntities: List<MusicBrainzEntityType>
    get() = listOf(
        MusicBrainzEntityType.AREA,
        MusicBrainzEntityType.ARTIST,
        MusicBrainzEntityType.EVENT,
        MusicBrainzEntityType.GENRE,
        MusicBrainzEntityType.INSTRUMENT,
        MusicBrainzEntityType.LABEL,
        MusicBrainzEntityType.PLACE,
        MusicBrainzEntityType.RECORDING,
        MusicBrainzEntityType.RELEASE,
        MusicBrainzEntityType.RELEASE_GROUP,
        MusicBrainzEntityType.SERIES,
        MusicBrainzEntityType.WORK,
        MusicBrainzEntityType.URL,
    )

val collaboratableEntities: List<MusicBrainzEntityType>
    get() = listOf(
        MusicBrainzEntityType.RECORDING,
        MusicBrainzEntityType.RELEASE,
        MusicBrainzEntityType.RELEASE_GROUP,
    )

fun String.toMusicBrainzEntityType(): MusicBrainzEntityType? =
    MusicBrainzEntityType.entries.firstOrNull { this == it.resourceUri }

/**
 * The uri resource to use for query/lookup request to MusicBrainz.
 * And for deeplinking.
 */
val MusicBrainzEntityType.resourceUri: String
    get() = when (this) {
        MusicBrainzEntityType.AREA -> RESOURCE_AREA
        MusicBrainzEntityType.ARTIST -> RESOURCE_ARTIST
        MusicBrainzEntityType.COLLECTION -> RESOURCE_COLLECTION
        MusicBrainzEntityType.EVENT -> RESOURCE_EVENT
        MusicBrainzEntityType.GENRE -> RESOURCE_GENRE
        MusicBrainzEntityType.INSTRUMENT -> RESOURCE_INSTRUMENT
        MusicBrainzEntityType.LABEL -> RESOURCE_LABEL
        MusicBrainzEntityType.PLACE -> RESOURCE_PLACE
        MusicBrainzEntityType.RECORDING -> RESOURCE_RECORDING
        MusicBrainzEntityType.RELEASE -> RESOURCE_RELEASE
        MusicBrainzEntityType.RELEASE_GROUP -> RESOURCE_RELEASE_GROUP
        MusicBrainzEntityType.SERIES -> RESOURCE_SERIES
        MusicBrainzEntityType.WORK -> RESOURCE_WORK
        MusicBrainzEntityType.URL -> RESOURCE_URL
    }

/**
 * The plural uri resource to use for PUT/DELETE collection requests to MusicBrainz.
 */
val MusicBrainzEntityType.resourceUriPlural: String
    get() = when (this) {
        MusicBrainzEntityType.AREA -> PLURAL_RESOURCE_AREA
        MusicBrainzEntityType.ARTIST -> PLURAL_RESOURCE_ARTIST
        MusicBrainzEntityType.COLLECTION -> PLURAL_RESOURCE_COLLECTION
        MusicBrainzEntityType.EVENT -> PLURAL_RESOURCE_EVENT
        MusicBrainzEntityType.GENRE -> PLURAL_RESOURCE_GENRE
        MusicBrainzEntityType.INSTRUMENT -> PLURAL_RESOURCE_INSTRUMENT
        MusicBrainzEntityType.LABEL -> PLURAL_RESOURCE_LABEL
        MusicBrainzEntityType.PLACE -> PLURAL_RESOURCE_PLACE
        MusicBrainzEntityType.RECORDING -> PLURAL_RESOURCE_RECORDING
        MusicBrainzEntityType.RELEASE -> PLURAL_RESOURCE_RELEASE
        MusicBrainzEntityType.RELEASE_GROUP -> PLURAL_RESOURCE_RELEASE_GROUP
        MusicBrainzEntityType.SERIES -> PLURAL_RESOURCE_SERIES
        MusicBrainzEntityType.WORK -> PLURAL_RESOURCE_WORK
        MusicBrainzEntityType.URL -> PLURAL_RESOURCE_URL
    }
