package ly.david.mbjc.data.network

/**
 * These are resources available for lookup requests. Many of them are query-able as well.
 */
internal enum class MusicBrainzResource(val displayText: String, val resourceName: String) {
    AREA("Area", "area"),
    ARTIST("Artist", "artist"),
    EVENT("Event", "event"),
    GENRE("Genre", "genre"),
    INSTRUMENT("Instrument", "instrument"),
    LABEL("Label", "label"),
    PLACE("Place", "place"),
    RECORDING("Recording", "recording"),
    RELEASE("Release", "release"),
    RELEASE_GROUP("Release Group", "release-group"),
    SERIES("Series", "series"),
    WORK("Work", "work"),
    URL("URL", "url")
}
