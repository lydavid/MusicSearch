package ly.david.musicsearch.shared.feature.nowplaying

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.history.NowPlayingHistory
import ly.david.musicsearch.shared.domain.nowplaying.NowPlayingHistoryRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val ANDROID_SYSTEM_INTELLIGENCE_PACKAGE_NAME = "com.google.android.as"
private const val ANDROID_TITLE_KEY = "android.title"

class NowPlayingNotificationListener : NotificationListenerService(), KoinComponent {

    private val coroutineScope: CoroutineScope by inject()
    private val nowPlayingHistoryRepository: NowPlayingHistoryRepository by inject()

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        // Expected format: Better Call Saul Main Title Theme (Extended) by Little Barrie
        val titleAndArtist = sbn?.notification?.extras?.getString(ANDROID_TITLE_KEY) ?: return
        val packageName = sbn.packageName

        if (packageName == ANDROID_SYSTEM_INTELLIGENCE_PACKAGE_NAME) {
            coroutineScope.launch {
                nowPlayingHistoryRepository.upsert(
                    NowPlayingHistory(
                        raw = titleAndArtist,
                    ),
                )
            }
        }
    }
}
