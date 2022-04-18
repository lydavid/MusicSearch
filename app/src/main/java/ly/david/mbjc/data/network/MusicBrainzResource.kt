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

    @Json(name = "release-group")
    RELEASE_GROUP("Release Group", "release-group"),

    @Json(name = "series")
    SERIES("Series", "series"),

    @Json(name = "work")
    WORK("Work", "work"),

    @Json(name = "url")
    URL("URL", "url")
}
