package ly.david.musicsearch.android.app.di

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.annotation.ExperimentalCoilApi
import coil.test.FakeImageLoaderEngine
import org.koin.dsl.module

@OptIn(ExperimentalCoilApi::class)
val testImageModule = module {
    single {
        ImageLoaderFactory {

            val drawable = object : ColorDrawable(Color.RED) {
                override fun getIntrinsicWidth() = 500
                override fun getIntrinsicHeight() = 500
            }

            val engine = FakeImageLoaderEngine.Builder()
                .default(drawable)
                .build()
            val imageLoader = ImageLoader.Builder(get())
                .components { add(engine) }
                .build()
            imageLoader
        }
    }
}
