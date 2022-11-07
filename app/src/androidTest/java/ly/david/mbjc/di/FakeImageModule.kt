package ly.david.mbjc.di

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import coil.ComponentRegistry
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.DataSource
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.DefaultRequestOptions
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import kotlinx.coroutines.CompletableDeferred

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ImageModule::class]
)
internal object FakeImageModule {

    @Provides
    @Singleton
    fun providesImageLoaderFactory(): ImageLoaderFactory = ImageLoaderFactory {
        FakeImageLoader()
    }

    /**
     * From: https://coil-kt.github.io/coil/image_loaders/#testing
     */
    class FakeImageLoader : ImageLoader {

        override val defaults = DefaultRequestOptions()
        override val components = ComponentRegistry()
        override val memoryCache: MemoryCache? get() = null
        override val diskCache: DiskCache? get() = null

        override fun enqueue(request: ImageRequest): Disposable {
            // Always call onStart before onSuccess.
            request.target?.onStart(request.placeholder)
            val result = ColorDrawable(Color.MAGENTA)
            request.target?.onSuccess(result)
            return object : Disposable {
                override val job = CompletableDeferred(newResult(request, result))
                override val isDisposed get() = true
                override fun dispose() {}
            }
        }

        override suspend fun execute(request: ImageRequest): ImageResult {
            return newResult(request, ColorDrawable(Color.MAGENTA))
        }

        private fun newResult(request: ImageRequest, drawable: Drawable): SuccessResult {
            return SuccessResult(
                drawable = drawable,
                request = request,
                dataSource = DataSource.MEMORY_CACHE
            )
        }

        override fun newBuilder() = throw UnsupportedOperationException()

        override fun shutdown() {}
    }
}
