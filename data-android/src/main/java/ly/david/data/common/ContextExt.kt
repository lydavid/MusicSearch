package ly.david.data.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUri
import ly.david.musicsearch.core.models.place.Coordinates
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_BASE_URL

// TODO: wrap url lookup with a context-agnostic class
//  then only android implementation uses context
/**
 * Launches web browser for MusicBrainz's page for [entity] with [musicBrainzId].
 */
fun Context.lookupInBrowser(entity: MusicBrainzEntity, musicBrainzId: String) {
    openUrl("$MUSIC_BRAINZ_BASE_URL/${entity.resourceUri}/$musicBrainzId")
}

// TODO: we could open map in google maps or open street maps for jvm
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
        },
    )
}

private fun Context.openUrl(url: String) {
    startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        },
    )
}
