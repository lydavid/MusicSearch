package ly.david.data.navigation

import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RESOURCE_AREA
import ly.david.data.network.RESOURCE_ARTIST
import ly.david.data.network.RESOURCE_EVENT
import ly.david.data.network.RESOURCE_GENRE
import ly.david.data.network.RESOURCE_INSTRUMENT
import ly.david.data.network.RESOURCE_LABEL
import ly.david.data.network.RESOURCE_PLACE
import ly.david.data.network.RESOURCE_RECORDING
import ly.david.data.network.RESOURCE_RELEASE
import ly.david.data.network.RESOURCE_RELEASE_GROUP
import ly.david.data.network.RESOURCE_SERIES
import ly.david.data.network.RESOURCE_URL
import ly.david.data.network.RESOURCE_WORK

private const val TOP_LEVEL_LOOKUP = "lookup"
private const val TOP_LEVEL_HISTORY = "history"
private const val TOP_LEVEL_SETTINGS = "settings"
private const val TOP_LEVEL_EXPERIMENTAL = "experimental"

/**
 * This divider should be the same used for dividing parameters such as {artistId} passed to navigation.
 */
private const val DIVIDER = "/"

/**
 * A navigation destination in the app.
 * The [Destination] enum is mostly for compile-time safety.
 *
 * @param route The underlying route that should be passed to navigation components.
 */
enum class Destination(val route: String) {
    LOOKUP(TOP_LEVEL_LOOKUP),
    LOOKUP_AREA("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_AREA"),
    LOOKUP_ARTIST("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_ARTIST"),
    LOOKUP_EVENT("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_EVENT"),
    LOOKUP_RELEASE_GROUP("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_RELEASE_GROUP"),
    LOOKUP_RELEASE("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_RELEASE"),
    LOOKUP_RECORDING("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_RECORDING"),
    LOOKUP_WORK("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_WORK"),
    LOOKUP_PLACE("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_PLACE"),
    LOOKUP_INSTRUMENT("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_INSTRUMENT"),
    LOOKUP_LABEL("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_LABEL"),
    LOOKUP_SERIES("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_SERIES"),
    LOOKUP_GENRE("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_GENRE"),
    LOOKUP_URL("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_URL"),

    HISTORY(TOP_LEVEL_HISTORY),
    SETTINGS(TOP_LEVEL_SETTINGS),

    EXPERIMENTAL(TOP_LEVEL_EXPERIMENTAL),
    EXPERIMENTAL_SETTINGS("$TOP_LEVEL_EXPERIMENTAL$DIVIDER" + "SETTINGS"),
    EXPERIMENTAL_SPOTIFY("$TOP_LEVEL_EXPERIMENTAL$DIVIDER" + "SPOTIFY")
}

fun MusicBrainzResource.toDestination() =
    when (this) {
        MusicBrainzResource.AREA -> Destination.LOOKUP_AREA
        MusicBrainzResource.ARTIST -> Destination.LOOKUP_ARTIST
        MusicBrainzResource.EVENT -> Destination.LOOKUP_EVENT
        MusicBrainzResource.GENRE -> Destination.LOOKUP_GENRE
        MusicBrainzResource.INSTRUMENT -> Destination.LOOKUP_INSTRUMENT
        MusicBrainzResource.LABEL -> Destination.LOOKUP_LABEL
        MusicBrainzResource.PLACE -> Destination.LOOKUP_PLACE
        MusicBrainzResource.RECORDING -> Destination.LOOKUP_RECORDING
        MusicBrainzResource.RELEASE -> Destination.LOOKUP_RELEASE
        MusicBrainzResource.RELEASE_GROUP -> Destination.LOOKUP_RELEASE_GROUP
        MusicBrainzResource.SERIES -> Destination.LOOKUP_SERIES
        MusicBrainzResource.WORK -> Destination.LOOKUP_WORK
        MusicBrainzResource.URL -> Destination.LOOKUP_URL
    }

/**
 * Get the route before any [DIVIDER].
 */
fun String.getTopLevelRoute(): String = this.split(DIVIDER).first()

/**
 * If not acting on [Destination.route], consider [getTopLevelRoute] first.
 */
fun String.getTopLevelDestination(): Destination = when (this) {
    TOP_LEVEL_HISTORY -> Destination.HISTORY
    TOP_LEVEL_EXPERIMENTAL -> Destination.EXPERIMENTAL
    TOP_LEVEL_SETTINGS -> Destination.SETTINGS
    else -> Destination.LOOKUP
}
