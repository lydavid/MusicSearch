package ly.david.data.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import ly.david.data.Coordinates
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.api.MUSIC_BRAINZ_BASE_URL
import ly.david.data.network.resourceUri

/**
 * Launches web browser for MusicBrainz's page for [entity] with [musicBrainzId].
 */
fun Context.lookupInBrowser(entity: MusicBrainzEntity, musicBrainzId: String) {
    startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("$MUSIC_BRAINZ_BASE_URL/${entity.resourceUri}/$musicBrainzId")
        }
    )
}

private const val ZOOM_LEVEL = 16

/**
 * Launch user's map application with given [coordinates].
 */
fun Context.showMap(coordinates: Coordinates, label: String?) {
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
fun Context.openUrl(url: String) {
    startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
    )
}
