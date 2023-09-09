package ly.david.mbjc.services

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.data.room.history.nowplaying.NowPlayingHistoryDao
import ly.david.data.room.history.nowplaying.NowPlayingHistoryRoomModel
import org.koin.android.ext.android.inject

private const val ANDROID_SYSTEM_INTELLIGENCE_PACKAGE_NAME = "com.google.android.as"
private const val ANDROID_TITLE_KEY = "android.title"

//@AndroidEntryPoint
class NowPlayingNotificationListener : NotificationListenerService() {

    private val coroutineScope: CoroutineScope by inject()
    private val nowPlayingHistoryDao: NowPlayingHistoryDao by inject()

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        // Expected format: Better Call Saul Main Title Theme (Extended) by Little Barrie
        val titleAndArtist = sbn?.notification?.extras?.getString(ANDROID_TITLE_KEY) ?: return
        val packageName = sbn.packageName

        if (packageName == ANDROID_SYSTEM_INTELLIGENCE_PACKAGE_NAME) {
            coroutineScope.launch {
                nowPlayingHistoryDao.insertReplace(
                    NowPlayingHistoryRoomModel(
                        raw = titleAndArtist,
                    )
                )
            }
        }
    }
}
