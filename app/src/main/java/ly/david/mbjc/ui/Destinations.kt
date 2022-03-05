package ly.david.mbjc.ui

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
 * The underlying [route] is what should be passed to navigation components.
 */
enum class Destination(val route: String) {
    LOOKUP(TOP_LEVEL_LOOKUP),

    LOOKUP_ARTIST("$TOP_LEVEL_LOOKUP$DIVIDER$ARTIST"),
    LOOKUP_RELEASE_GROUP("$TOP_LEVEL_LOOKUP$DIVIDER$RELEASE_GROUP"),
    LOOKUP_RELEASE("$TOP_LEVEL_LOOKUP$DIVIDER$RELEASE"),

    HISTORY(TOP_LEVEL_HISTORY)
}

/**
 * Get the route before any [DIVIDER].
 */
fun String.getTopLevelRoute(): String = this.split(DIVIDER).first()

/**
 * If not acting on [Destination.route], consider [getTopLevelRoute] first.
 */
fun String.getTopLevelDestination(): Destination = when(this) {
    TOP_LEVEL_HISTORY -> Destination.HISTORY
    else -> Destination.LOOKUP
}
