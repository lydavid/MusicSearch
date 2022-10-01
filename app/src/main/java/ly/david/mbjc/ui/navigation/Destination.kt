package ly.david.mbjc.ui.navigation

import ly.david.mbjc.data.network.MusicBrainzResource

private const val TOP_LEVEL_LOOKUP = "lookup"
private const val TOP_LEVEL_HISTORY = "history"

/**
 * This divider should be the same used for dividing parameters such as {artistId} passed to navigation.
 */
private const val DIVIDER = "/"

private const val ARTIST = "artist"
private const val RELEASE_GROUP = "release-group" // TODO: besides release_group, these match exactly to MusicBrainzResource
private const val RELEASE = "release"
private const val RECORDING = "recording"
private const val AREA = "area"
private const val PLACE = "place"
private const val WORK = "work"
private const val LABEL = "label"
private const val INSTRUMENT = "instrument"
private const val EVENT = "event"
private const val SERIES = "series"
private const val GENRE = "genre"
private const val URL = "url"

/**
 * A navigation destination in the app.
 * The [Destination] enum is mostly for compile-time safety.
 *
 * @param route The underlying route that should be passed to navigation components.
 * @param musicBrainzResource The associated MusicBrainz resource, if any.
 */
internal enum class Destination(val route: String, val musicBrainzResource: MusicBrainzResource?) {
    LOOKUP(TOP_LEVEL_LOOKUP, null),

    LOOKUP_ARTIST("$TOP_LEVEL_LOOKUP$DIVIDER$ARTIST", MusicBrainzResource.ARTIST),
    LOOKUP_RELEASE_GROUP("$TOP_LEVEL_LOOKUP$DIVIDER$RELEASE_GROUP", MusicBrainzResource.RELEASE_GROUP),
    LOOKUP_RELEASE("$TOP_LEVEL_LOOKUP$DIVIDER$RELEASE", MusicBrainzResource.RELEASE),
    LOOKUP_RECORDING("$TOP_LEVEL_LOOKUP$DIVIDER$RECORDING", MusicBrainzResource.RECORDING),
    LOOKUP_WORK("$TOP_LEVEL_LOOKUP$DIVIDER$WORK", MusicBrainzResource.WORK),

    LOOKUP_AREA("$TOP_LEVEL_LOOKUP$DIVIDER$AREA", MusicBrainzResource.AREA),
    LOOKUP_PLACE("$TOP_LEVEL_LOOKUP$DIVIDER$PLACE", MusicBrainzResource.PLACE),
    LOOKUP_INSTRUMENT("$TOP_LEVEL_LOOKUP$DIVIDER$INSTRUMENT", MusicBrainzResource.INSTRUMENT),
    LOOKUP_LABEL("$TOP_LEVEL_LOOKUP$DIVIDER$LABEL", MusicBrainzResource.LABEL),

    LOOKUP_EVENT("$TOP_LEVEL_LOOKUP$DIVIDER$EVENT", MusicBrainzResource.EVENT),
    LOOKUP_SERIES("$TOP_LEVEL_LOOKUP$DIVIDER$SERIES", MusicBrainzResource.SERIES),
    LOOKUP_GENRE("$TOP_LEVEL_LOOKUP$DIVIDER$GENRE", MusicBrainzResource.GENRE),
    LOOKUP_URL("$TOP_LEVEL_LOOKUP$DIVIDER$URL", MusicBrainzResource.URL),

    HISTORY(TOP_LEVEL_HISTORY, null)
}

internal fun MusicBrainzResource.toDestination() =
    when (this) {
        MusicBrainzResource.ARTIST -> Destination.LOOKUP_ARTIST
        MusicBrainzResource.RELEASE_GROUP -> Destination.LOOKUP_RELEASE_GROUP
        MusicBrainzResource.RELEASE -> Destination.LOOKUP_RELEASE
        MusicBrainzResource.RECORDING -> Destination.LOOKUP_RECORDING
        MusicBrainzResource.AREA -> Destination.LOOKUP_AREA
        MusicBrainzResource.PLACE -> Destination.LOOKUP_PLACE
        MusicBrainzResource.INSTRUMENT -> Destination.LOOKUP_INSTRUMENT
        MusicBrainzResource.LABEL -> Destination.LOOKUP_LABEL
        MusicBrainzResource.WORK -> Destination.LOOKUP_WORK

        MusicBrainzResource.EVENT -> Destination.LOOKUP_EVENT
        // TODO: everything below
        MusicBrainzResource.SERIES -> Destination.LOOKUP_SERIES

        MusicBrainzResource.GENRE -> Destination.LOOKUP_GENRE
        MusicBrainzResource.URL -> Destination.LOOKUP_URL
    }

/**
 * Get the route before any [DIVIDER].
 */
internal fun String.getTopLevelRoute(): String = this.split(DIVIDER).first()

/**
 * If not acting on [Destination.route], consider [getTopLevelRoute] first.
 */
internal fun String.getTopLevelDestination(): Destination = when (this) {
    TOP_LEVEL_HISTORY -> Destination.HISTORY
    else -> Destination.LOOKUP
}
