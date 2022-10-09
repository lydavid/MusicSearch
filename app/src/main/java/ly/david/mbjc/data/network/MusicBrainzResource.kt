package ly.david.mbjc.data.network

import com.squareup.moshi.Json

// TODO: displayText should be translated
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

    // Not searchable, but lookupable
    @Json(name = "genre")
    GENRE("Genre", "genre"),

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

    // Note that target-type uses release_group, while uri uses release-group.
    // For our internal resource, we will use release-group.
    @Json(name = "release_group")
    RELEASE_GROUP("Release Group", "release-group"),

    @Json(name = "series")
    SERIES("Series", "series"),

    @Json(name = "work")
    WORK("Work", "work"),

    // Not searchable, but lookupable
    @Json(name = "url")
    URL("URL", "url"),

    // Other searchable: annotation, tag, cd stub, editor, documentation

    // Other non-core resources with API: rating, tag, collection

    // Other lookupable: discid, isrc, iswc
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

internal fun String.toMusicBrainzResource(): MusicBrainzResource? =
    MusicBrainzResource.values().firstOrNull { this == it.resourceName }
