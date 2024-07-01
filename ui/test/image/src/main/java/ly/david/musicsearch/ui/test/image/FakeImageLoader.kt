package ly.david.musicsearch.ui.test.image

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import coil.ComponentRegistry
import coil.ImageLoader
import coil.decode.DataSource
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.DefaultRequestOptions
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import kotlinx.coroutines.CompletableDeferred

/**
 * The one from https://coil-kt.github.io/coil/testing/ is not working but
 * this one does work: https://github.com/slackhq/circuit/pull/204/files#diff-90466f88d1428d95e3c54c80835a3544eb90ea3756edf479ee144d41c2ce8707
 */
class FakeImageLoader : ImageLoader {
    private val drawable by lazy { ColorDrawable(Color.BLUE) }

    override val defaults = DefaultRequestOptions()
    override val components = ComponentRegistry()
    override val memoryCache: MemoryCache?
        get() = null
    override val diskCache: DiskCache?
        get() = null

    override fun enqueue(request: ImageRequest): Disposable {
        request.target?.onStart(request.placeholder)
        request.target?.onSuccess(drawable)

        return object : Disposable {
            override val job = CompletableDeferred(
                newResult(
                    request,
                    drawable,
                ),
            )
            override val isDisposed
                get() = true

            override fun dispose() = Unit
        }
    }

    override suspend fun execute(request: ImageRequest): ImageResult = newResult(
        request,
        drawable,
    )

    private fun newResult(
        request: ImageRequest,
        drawable: Drawable,
    ): SuccessResult {
        return SuccessResult(
            drawable = drawable,
            request = request,
            dataSource = DataSource.MEMORY_CACHE,
        )
    }

    override fun newBuilder() = throw UnsupportedOperationException()

    override fun shutdown() = Unit
}
