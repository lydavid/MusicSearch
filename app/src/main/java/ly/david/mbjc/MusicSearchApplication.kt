package ly.david.mbjc

import android.app.Application
import coil.Coil
import coil.ImageLoaderFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ly.david.data.coverart.CoverArtDataModule
import ly.david.data.di.auth.AuthStoreModule
import ly.david.data.di.coroutines.coroutinesScopesModule
import ly.david.data.di.logging.loggingModule
import ly.david.data.di.musicbrainz.musicBrainzAuthModule
import ly.david.data.di.network.networkModule
import ly.david.data.di.preferences.preferencesDataStoreModule
import ly.david.data.di.room.databaseDaoModule
import ly.david.data.di.room.databaseModule
import ly.david.data.domain.DomainDataModule
import ly.david.data.musicbrainz.auth.MusicBrainzDataModule
import ly.david.data.spotify.di.SpotifyDataModule
import ly.david.mbjc.di.appDataModule
import ly.david.ui.collections.CollectionUiModule
import ly.david.ui.common.CommonUiModule
import ly.david.ui.history.HistoryUiModule
import ly.david.ui.image.di.imageModule
import ly.david.ui.nowplaying.NowPlayingUiModule
import ly.david.ui.settings.SettingsUiModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

internal class MusicSearchApplication : Application() {

    private val imageLoaderFactory: ImageLoaderFactory by inject()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree(FirebaseCrashlytics.getInstance()))
        }

        startKoin {
            androidLogger()
            androidContext(this@MusicSearchApplication)
            modules(
                AndroidAppModule().module,
                appDataModule,
//                authModule,
//                coroutinesDispatchersModule,
                coroutinesScopesModule,
                loggingModule,
                musicBrainzAuthModule,
                networkModule,
                preferencesDataStoreModule,
                databaseDaoModule,
                databaseModule,
                imageModule,
                CoverArtDataModule().module,
                DomainDataModule().module,
                MusicBrainzDataModule().module,
                SpotifyDataModule().module,
                AuthStoreModule().module,
                CollectionUiModule().module,
                CommonUiModule().module,
                HistoryUiModule().module,
                NowPlayingUiModule().module,
                SettingsUiModule().module,
            )
        }

        Coil.setImageLoader(imageLoaderFactory)
    }
}

private class CrashlyticsTree(
    private val firebaseCrashlytics: FirebaseCrashlytics,
) : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        firebaseCrashlytics.log(message)
    }
}
