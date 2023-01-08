package ly.david.mbjc.di

import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

@Module
@InstallIn(SingletonComponent::class)
internal object ImageModule {

    @Provides
    @Singleton
    fun providesImageLoaderFactory(
        @ApplicationContext context: Context,
    ) = ImageLoaderFactory {
        ImageLoader.Builder(context)
            .okHttpClient {
                OkHttpClient.Builder()
                    // Make sure we don't use okhttp cache.
                    .addInterceptor(RequestHeaderInterceptor("Cache-Control", "no-cache"))
                    .build()
            }
            .build()
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
}
