package ly.david.mbjc.ui.experimental.nowplaying

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.data.common.ApplicationScope
import ly.david.data.room.history.nowplaying.NowPlayingHistoryDao
import ly.david.data.room.history.nowplaying.NowPlayingHistoryRoomModel
import timber.log.Timber

@AndroidEntryPoint
internal class NowPlayingNotificationListener : NotificationListenerService() {

    @Inject
    @ApplicationScope
    lateinit var coroutineScope: CoroutineScope

    @Inject
    lateinit var nowPlayingHistoryDao: NowPlayingHistoryDao

    override fun onCreate() {
        super.onCreate()
        Timber.d("hello world!!")

        coroutineScope.launch {
            nowPlayingHistoryDao.insert(
                NowPlayingHistoryRoomModel(
                    title = "onCreate",
                    text = Math.random().toString()
                )
            )
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val title = sbn?.notification?.extras?.getString("android.title") ?: "was empty"
        val text = sbn?.notification?.extras?.getString("android.text") ?: "was empty"
        val packageName = sbn?.packageName
        Timber.d("android.title: $title")
        Timber.d("android.text: $text")
        Timber.d("packageName: $packageName")
        coroutineScope.launch {
            nowPlayingHistoryDao.insert(
                NowPlayingHistoryRoomModel(
                    title = "$packageName: $title",
                    text = text
                )
            )
        }
    }
}
