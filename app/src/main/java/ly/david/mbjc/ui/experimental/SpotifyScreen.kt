package ly.david.mbjc.ui.experimental

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ly.david.data.common.ifNotNull
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.theme.TextStyles.getCardTitleTextStyle

object BroadcastTypes {
    private const val SPOTIFY_PACKAGE = "com.spotify.music"
    const val PLAYBACK_STATE_CHANGED = "$SPOTIFY_PACKAGE.playbackstatechanged"
    const val QUEUE_CHANGED = "$SPOTIFY_PACKAGE.queuechanged"
    const val METADATA_CHANGED = "$SPOTIFY_PACKAGE.metadatachanged"
}

data class SpotifyMetadata(
    val trackId: String? = null,
    val artistName: String? = null,
    val albumName: String? = null,
    val trackName: String? = null,
    val trackLengthInSec: Int? = null,
    val timeSentInMs: Long? = null
)

@Composable
fun SpotifyBroadcastReceiver(
    onMetadataChange: (SpotifyMetadata) -> Unit
) {
    // Grab the current context in this part of the UI tree
    val context = LocalContext.current

    // If either context or systemAction changes, unregister and register again
    DisposableEffect(context) {
        val intentFilter = IntentFilter().apply {
            addAction(BroadcastTypes.METADATA_CHANGED)
            addAction(BroadcastTypes.QUEUE_CHANGED)
            addAction(BroadcastTypes.PLAYBACK_STATE_CHANGED)
        }

        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                // This is sent with all broadcasts, regardless of type. The value is taken from
                // System.currentTimeMillis(), which you can compare to in order to determine how
                // old the event is.
                val timeSentInMs = intent.getLongExtra("timeSent", 0L)
                Log.d("Remove This", "timeSentInMs=$timeSentInMs")
                when (intent.action) {
                    BroadcastTypes.METADATA_CHANGED -> {
                        val trackId = intent.getStringExtra("id")
                        val artistName = intent.getStringExtra("artist")
                        val albumName = intent.getStringExtra("album")
                        val trackName = intent.getStringExtra("track")
                        val trackLengthInSec = intent.getIntExtra("length", 0)
                        onMetadataChange(
                            SpotifyMetadata(
                                trackId = trackId,
                                artistName = artistName,
                                albumName = albumName,
                                trackName = trackName,
                                trackLengthInSec = trackLengthInSec,
                                timeSentInMs = timeSentInMs
                            )
                        )
                    }
                    BroadcastTypes.PLAYBACK_STATE_CHANGED -> {
                        val playing = intent.getBooleanExtra("playing", false)
                        val positionInMs = intent.getIntExtra("playbackPosition", 0)
                        // Do something with extracted information
                        Log.d("Remove This", "playing=$playing")
                        Log.d("Remove This", "positionInMs=$positionInMs")
                    }
                    BroadcastTypes.QUEUE_CHANGED -> {
                        // Sent only as a notification, your app may want to respond accordingly.
                    }
                }
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

@Composable
internal fun SpotifyScreen(
    searchMusicBrainz: (query: String, id: MusicBrainzResource) -> Unit = { _, _ -> },
) {

    var metadata: SpotifyMetadata by remember { mutableStateOf(SpotifyMetadata()) }

    SpotifyBroadcastReceiver {
        metadata = it
    }

    Surface {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            metadata.trackId.ifNotNullOrEmpty {
                Text(
                    text = it,
                    style = getCardTitleTextStyle()
                )
            }

            metadata.artistName.ifNotNullOrEmpty { artistName ->
                Text(
                    modifier = Modifier.clickable {
                        searchMusicBrainz(artistName, MusicBrainzResource.ARTIST)
                    },
                    text = artistName,
                    style = getCardTitleTextStyle()
                )

                metadata.albumName.ifNotNullOrEmpty { albumName ->
                    Text(
                        modifier = Modifier.clickable {
                            searchMusicBrainz("$albumName AND artist:$artistName", MusicBrainzResource.RELEASE_GROUP)
                        },
                        text = albumName,
                        style = getCardTitleTextStyle()
                    )
                }

                metadata.trackName.ifNotNullOrEmpty { trackName ->
                    Text(
                        modifier = Modifier.clickable {
                            searchMusicBrainz("$trackName AND artist:$artistName", MusicBrainzResource.RECORDING)
                        },
                        text = trackName,
                        style = getCardTitleTextStyle()
                    )
                }
            }

            metadata.trackLengthInSec.ifNotNull {
                Text(
                    text = it.toString(),
                    style = getCardTitleTextStyle()

                )
            }
            metadata.timeSentInMs.ifNotNull {
                Text(
                    text = it.toString(),
                    style = getCardTitleTextStyle()
                )
            }
        }
    }
}
