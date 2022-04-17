package ly.david.mbjc.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import ly.david.mbjc.data.network.MUSIC_BRAINZ_BASE_URL
import ly.david.mbjc.data.network.MusicBrainzResource

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
