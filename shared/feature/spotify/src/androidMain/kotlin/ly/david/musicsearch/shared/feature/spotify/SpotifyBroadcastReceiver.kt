package ly.david.musicsearch.shared.feature.spotify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.history.SpotifyHistory
import ly.david.musicsearch.shared.domain.spotify.SpotifyHistoryRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object BroadcastTypes {
    private const val SPOTIFY_PACKAGE = "com.spotify.music"
    const val PLAYBACK_STATE_CHANGED = "$SPOTIFY_PACKAGE.playbackstatechanged"
    const val QUEUE_CHANGED = "$SPOTIFY_PACKAGE.queuechanged"
    const val METADATA_CHANGED = "$SPOTIFY_PACKAGE.metadatachanged"
}

/**
 * [Docs](https://developer.spotify.com/documentation/android/tutorials/android-media-notifications).
 */
class SpotifyBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val spotifyHistoryRepository: SpotifyHistoryRepository by inject()

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
                val trackId = intent.getStringExtra("id") ?: return
                val artistName = intent.getStringExtra("artist")
                val albumName = intent.getStringExtra("album")
                val trackName = intent.getStringExtra("track")
                val trackLengthMilliseconds = intent.getIntExtra(
                    "length",
                    0,
                )
                spotifyHistoryRepository.insert(
                    SpotifyHistory(
                        trackId = trackId,
                        artistName = artistName,
                        albumName = albumName,
                        trackName = trackName,
                        trackLengthMilliseconds = trackLengthMilliseconds,
                        lastListened = Instant.fromEpochMilliseconds(timeSentInMs),
                    ),
                )
            }

            BroadcastTypes.PLAYBACK_STATE_CHANGED -> {
//                        val playing = intent.getBooleanExtra("playing", false)
//                        val playbackPosition = intent.getIntExtra("playbackPosition", 0)
            }

            BroadcastTypes.QUEUE_CHANGED -> {
                // Nothing at the moment.
            }
        }
    }
}
