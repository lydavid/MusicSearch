package ly.david.mbjc.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import ly.david.mbjc.data.Coordinates
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.api.MUSIC_BRAINZ_BASE_URL

/**
 * Launches web browser for MusicBrainz's page for [resource] with [musicBrainzId].
 */
internal fun Context.lookupInBrowser(resource: MusicBrainzResource, musicBrainzId: String) {
    startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("$MUSIC_BRAINZ_BASE_URL/${resource.resourceName}/${musicBrainzId}")
        }
    )
}

private const val ZOOM_LEVEL = 16

/**
 * Launch user's map application with given [coordinates].
 */
internal fun Context.showMap(coordinates: Coordinates, label: String?) {
    val latitude = coordinates.latitude ?: return
    val longitude = coordinates.longitude ?: return
    startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            val urlString = if (label.isNullOrEmpty()) {
                "geo:$latitude,$longitude?z=$ZOOM_LEVEL"
            } else {
                "geo:0,0?q=$latitude,$longitude($label)&z=$ZOOM_LEVEL"
            }
            data = Uri.parse(urlString)
        }
    )
}

/**
 * Opens a [url] in browser.
 *
 * Note that if a user has certain apps that can handle this intent, it will deeplink into it instead.
 * Eg. if the user has Spotify, and clicks on [https://open.spotify.com/artist/38WbKH6oKAZskBhqDFA8Uj],
 * it will open this artist in Spotify.
 */
internal fun Context.openUrl(url: String) {
    startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
    )
}
