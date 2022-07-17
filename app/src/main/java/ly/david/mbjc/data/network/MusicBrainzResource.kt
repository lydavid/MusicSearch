package ly.david.mbjc.data.network

import com.squareup.moshi.Json

/**
 * These are resources available for lookup requests. Many of them are query-able as well.
 *
 * @param resourceName The resource to use for query/lookup request to MusicBrainz.
 */
internal enum class MusicBrainzResource(val displayText: String, val resourceName: String) {
    @Json(name = "area")
    AREA("Area", "area"),

    @Json(name = "artist")
    ARTIST("Artist", "artist"),

    @Json(name = "event")
    EVENT("Event", "event"),

    @Json(name = "instrument")
    INSTRUMENT("Instrument", "instrument"),

    @Json(name = "label")
    LABEL("Label", "label"),

    @Json(name = "place")
    PLACE("Place", "place"),

    @Json(name = "recording")
    RECORDING("Recording", "recording"),

    @Json(name = "release")
    RELEASE("Release", "release"),

    @Json(name = "release-group")
    RELEASE_GROUP("Release Group", "release-group"),

    @Json(name = "series")
    SERIES("Series", "series"),

    @Json(name = "work")
    WORK("Work", "work"),

    // These are not searchable, but they are lookupable
    @Json(name = "genre")
    GENRE("Genre", "genre"),

    // These are not searchable, but they are lookupable
    @Json(name = "url")
    URL("URL", "url"),

    // Other searchable: annotation, tag, cd stub, editor, documentation

    // Other non-core resources with API: rating, tag, collection

    // Other lookupable: discid, isrc, iswc

    // TODO: for non-first-class resources, maybe they shouldn't be in this enum
}

internal val searchableResources: List<MusicBrainzResource>
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
