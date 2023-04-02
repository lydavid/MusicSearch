package ly.david.mbjc.di

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.annotation.ExperimentalCoilApi
import coil.test.FakeImageLoaderEngine
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ImageModule::class]
)
internal object FakeImageModule {

    @OptIn(ExperimentalCoilApi::class)
    @Provides
    @Singleton
    fun providesImageLoaderFactory(
        @ApplicationContext context: Context,
    ) = ImageLoaderFactory {

        val drawable = object : ColorDrawable(Color.RED) {
            override fun getIntrinsicWidth() = 500
            override fun getIntrinsicHeight() = 500
        }

        val engine = FakeImageLoaderEngine.Builder()
            .default(drawable)
            .build()
        val imageLoader = ImageLoader.Builder(context)
            .components { add(engine) }
            .build()
        imageLoader
    }
}
