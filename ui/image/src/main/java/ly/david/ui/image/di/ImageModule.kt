package ly.david.ui.image.di

import coil.ImageLoader
import coil.ImageLoaderFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.koin.dsl.module

private class RequestHeaderInterceptor(
    private val name: String,
    private val value: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(name, value)
            .build()
        return chain.proceed(request)
    }
}

val imageModule = module {
    single {
        ImageLoaderFactory {
            ImageLoader.Builder(get())
                .okHttpClient {
                    OkHttpClient.Builder()
                        // Make sure we don't use okhttp cache.
                        .addInterceptor(RequestHeaderInterceptor("Cache-Control", "no-cache"))
                        .build()
                }
                .build()
        }
    }
}
