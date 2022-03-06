package ly.david.mbjc.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import ly.david.mbjc.data.MUSIC_BRAINZ_BASE_URL
import ly.david.mbjc.data.network.MusicBrainzResource

internal fun Context.lookupInBrowser(resource: MusicBrainzResource, musicBrainzId: String) {
    startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("$MUSIC_BRAINZ_BASE_URL/${resource.resourceName}/${musicBrainzId}")
        }
    )
}
