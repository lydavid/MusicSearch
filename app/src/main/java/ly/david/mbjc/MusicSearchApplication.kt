package ly.david.mbjc

import android.app.Application
import coil.Coil
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
internal class MusicSearchApplication : Application() {

    @Inject
    lateinit var imageLoaderFactory: ImageLoaderFactory

    override fun onCreate() {
        super.onCreate()
        Coil.setImageLoader(imageLoaderFactory)
    }
}
