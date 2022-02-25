package ly.david.musicbrainzjetpackcompose.ui

private const val DIVIDER = "/"

private const val ARTIST = "artist"
private const val RELEASE_GROUP = "release-group"
private const val RELEASE = "release"

// TODO: Route enum? problem is that navBackStackEntry?.destination?.route stores a String,
//  so we'll always be using .name or a property
object Routes {

    // TODO: once we support searching other resources, figure out if it should be in same screen?
    //  can it feasibly use the same list items? onClick will be different
    /**
     * Search MusicBrainz for resources.
     * Right now, we only support searching artists.
     */
    const val SEARCH = "search"

    // TODO: Consider whether these screens will continue to be sub-routes of search
    //  if not, which top-level screen should we highlight on the nav drawer, if any?
    //  If these routes can be accessed from both search and history screens, then they don't actually fit under either.
    /**
     * Information about an artist, including all release groups by them.
     */
    const val LOOKUP_ARTIST = "$SEARCH$DIVIDER$ARTIST"

    /**
     * Information about a release group, including all releases under it.
     */
    const val LOOKUP_RELEASE_GROUP = "$SEARCH$DIVIDER$RELEASE_GROUP"

    /**
     * Information about a release, including all its tracks.
     */
    const val LOOKUP_RELEASE = "$SEARCH$DIVIDER$RELEASE"

    /**
     * Shows the user's most recently visited screens.
     */
    const val HISTORY = "history"

    fun getTopLevelRoute(route: String): String = route.split(DIVIDER).first()
}
