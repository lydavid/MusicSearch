package ly.david.musicsearch.domain

import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.RESOURCE_AREA
import ly.david.musicsearch.core.models.network.RESOURCE_ARTIST
import ly.david.musicsearch.core.models.network.RESOURCE_EVENT
import ly.david.musicsearch.core.models.network.RESOURCE_GENRE
import ly.david.musicsearch.core.models.network.RESOURCE_INSTRUMENT
import ly.david.musicsearch.core.models.network.RESOURCE_LABEL
import ly.david.musicsearch.core.models.network.RESOURCE_PLACE
import ly.david.musicsearch.core.models.network.RESOURCE_RECORDING
import ly.david.musicsearch.core.models.network.RESOURCE_RELEASE
import ly.david.musicsearch.core.models.network.RESOURCE_RELEASE_GROUP
import ly.david.musicsearch.core.models.network.RESOURCE_SERIES
import ly.david.musicsearch.core.models.network.RESOURCE_URL
import ly.david.musicsearch.core.models.network.RESOURCE_WORK

private const val TOP_LEVEL_LOOKUP = "lookup"
private const val TOP_LEVEL_HISTORY = "history"
private const val TOP_LEVEL_COLLECTIONS = "collections"
private const val TOP_LEVEL_SETTINGS = "settings"

private const val EXPERIMENTAL = "experimental"
private const val LICENSES = "licenses"
private const val NOWPLAYING = "nowplaying"

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
    LOOKUP_GENRE("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_GENRE"),
    LOOKUP_INSTRUMENT("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_INSTRUMENT"),
    LOOKUP_LABEL("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_LABEL"),
    LOOKUP_PLACE("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_PLACE"),
    LOOKUP_RECORDING("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_RECORDING"),
    LOOKUP_RELEASE("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_RELEASE"),
    LOOKUP_RELEASE_GROUP("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_RELEASE_GROUP"),
    LOOKUP_SERIES("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_SERIES"),
    LOOKUP_URL("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_URL"),
    LOOKUP_WORK("$TOP_LEVEL_LOOKUP$DIVIDER$RESOURCE_WORK"),

    HISTORY(TOP_LEVEL_HISTORY),

    COLLECTIONS(TOP_LEVEL_COLLECTIONS),

    SETTINGS(TOP_LEVEL_SETTINGS),
    SETTINGS_LICENSES("$TOP_LEVEL_SETTINGS$DIVIDER$LICENSES"),
    SETTINGS_NOWPLAYING("$TOP_LEVEL_SETTINGS$DIVIDER$NOWPLAYING"),
    EXPERIMENTAL_SPOTIFY("$TOP_LEVEL_SETTINGS$DIVIDER$EXPERIMENTAL$DIVIDER" + "SPOTIFY"),
}

fun MusicBrainzEntity.toLookupDestination() =
    when (this) {
        MusicBrainzEntity.AREA -> Destination.LOOKUP_AREA
        MusicBrainzEntity.ARTIST -> Destination.LOOKUP_ARTIST
        MusicBrainzEntity.EVENT -> Destination.LOOKUP_EVENT
        MusicBrainzEntity.GENRE -> Destination.LOOKUP_GENRE
        MusicBrainzEntity.INSTRUMENT -> Destination.LOOKUP_INSTRUMENT
        MusicBrainzEntity.LABEL -> Destination.LOOKUP_LABEL
        MusicBrainzEntity.PLACE -> Destination.LOOKUP_PLACE
        MusicBrainzEntity.RECORDING -> Destination.LOOKUP_RECORDING
        MusicBrainzEntity.RELEASE -> Destination.LOOKUP_RELEASE
        MusicBrainzEntity.RELEASE_GROUP -> Destination.LOOKUP_RELEASE_GROUP
        MusicBrainzEntity.SERIES -> Destination.LOOKUP_SERIES
        MusicBrainzEntity.WORK -> Destination.LOOKUP_WORK
        MusicBrainzEntity.URL -> Destination.LOOKUP_URL
        MusicBrainzEntity.COLLECTION -> Destination.COLLECTIONS
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
    TOP_LEVEL_COLLECTIONS -> Destination.COLLECTIONS
    TOP_LEVEL_SETTINGS -> Destination.SETTINGS
    else -> Destination.LOOKUP
}
