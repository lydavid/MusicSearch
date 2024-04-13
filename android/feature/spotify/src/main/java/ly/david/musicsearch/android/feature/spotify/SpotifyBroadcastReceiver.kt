package ly.david.musicsearch.android.feature.spotify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

private object BroadcastTypes {
    private const val SPOTIFY_PACKAGE = "com.spotify.music"
    const val PLAYBACK_STATE_CHANGED = "$SPOTIFY_PACKAGE.playbackstatechanged"
    const val QUEUE_CHANGED = "$SPOTIFY_PACKAGE.queuechanged"
    const val METADATA_CHANGED = "$SPOTIFY_PACKAGE.metadatachanged"
}

@Composable
internal fun SpotifyBroadcastReceiver(
    onMetadataChange: (SpotifyMetadata) -> Unit,
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
            override fun onReceive(
                context: Context,
                intent: Intent,
            ) {
                // This is sent with all broadcasts, regardless of type. The value is taken from
                // System.currentTimeMillis(), which you can compare to in order to determine how
                // old the event is.
                val timeSentInMs = intent.getLongExtra(
                    "timeSent",
                    0L,
                )
                when (intent.action) {
                    BroadcastTypes.METADATA_CHANGED -> {
                        val trackId = intent.getStringExtra("id")
                        val artistName = intent.getStringExtra("artist")
                        val albumName = intent.getStringExtra("album")
                        val trackName = intent.getStringExtra("track")
                        val trackLengthInSec = intent.getIntExtra(
                            "length",
                            0,
                        )
                        onMetadataChange(
                            SpotifyMetadata(
                                trackId = trackId,
                                artistName = artistName,
                                albumName = albumName,
                                trackName = trackName,
                                trackLengthInSec = trackLengthInSec,
                                timeSentInMs = timeSentInMs,
                            ),
                        )
                    }

                    BroadcastTypes.PLAYBACK_STATE_CHANGED -> {
                        // Nothing at the moment.
                    }

                    BroadcastTypes.QUEUE_CHANGED -> {
                        // Nothing at the moment.
                    }
                }
            }
        }

        ContextCompat.registerReceiver(
            context,
            broadcast,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED,
        )

        // When the effect leaves the Composition, remove the callback
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}
