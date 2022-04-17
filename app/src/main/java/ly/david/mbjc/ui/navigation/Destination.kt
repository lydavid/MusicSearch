package ly.david.mbjc.ui.navigation

import ly.david.mbjc.data.network.MusicBrainzResource

private const val TOP_LEVEL_LOOKUP = "lookup"
private const val TOP_LEVEL_HISTORY = "history"

/**
 * This divider should be the same used for dividing parameters such as {artistId} passed to navigation.
 */
private const val DIVIDER = "/"

private const val ARTIST = "artist"
private const val RELEASE_GROUP = "release-group"
private const val RELEASE = "release"

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

    HISTORY(TOP_LEVEL_HISTORY, null)
}

/**
 * Get the route before any [DIVIDER].
 */
internal fun String.getTopLevelRoute(): String = this.split(DIVIDER).first()

/**
 * If not acting on [Destination.route], consider [getTopLevelRoute] first.
 */
internal fun String.getTopLevelDestination(): Destination = when(this) {
    TOP_LEVEL_HISTORY -> Destination.HISTORY
    else -> Destination.LOOKUP
}
