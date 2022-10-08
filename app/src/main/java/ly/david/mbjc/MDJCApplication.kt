package ly.david.mbjc

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

@HiltAndroidApp
internal class MDJCApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .okHttpClient {
                OkHttpClient.Builder()
                    // Make sure we don't use okhttp cache.
                    .addInterceptor(RequestHeaderInterceptor("Cache-Control", "no-cache"))
                    .build()
            }
            // Seems to be default.
//            .memoryCache {
//                MemoryCache.Builder(this)
//                    .maxSizePercent(0.25)
//                    .build()
//            }
//            .diskCache {
//                DiskCache.Builder()
//                    .directory(cacheDir.resolve("image_cache"))
//                    .maxSizePercent(0.02)
//                    .build()
//            }
            .build()
    }
}

private class RequestHeaderInterceptor(
    private val name: String,
    private val value: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(name, value)
            .build()
        return chain.proceed(request)
    }
}
