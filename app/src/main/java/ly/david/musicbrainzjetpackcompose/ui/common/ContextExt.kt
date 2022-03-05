package ly.david.musicbrainzjetpackcompose.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import ly.david.musicbrainzjetpackcompose.data.MUSIC_BRAINZ_BASE_URL
import ly.david.musicbrainzjetpackcompose.data.MusicBrainzResource

internal fun Context.lookupInBrowser(resource: MusicBrainzResource, musicBrainzId: String) {
    startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("$MUSIC_BRAINZ_BASE_URL/${resource.resourceName}/${musicBrainzId}")
        }
    )
}
