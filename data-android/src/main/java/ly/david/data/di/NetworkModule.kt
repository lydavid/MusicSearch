package ly.david.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import ly.david.data.BuildConfig
import ly.david.data.network.USER_AGENT
import ly.david.data.network.USER_AGENT_VALUE
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

private const val ACCEPT = "Accept"
private const val ACCEPT_VALUE = "application/json"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        musicBrainzAuthenticator: MusicBrainzAuthenticator
    ): OkHttpClient {
        val clientBuilder = OkHttpClient().newBuilder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
            clientBuilder.addInterceptor(loggingInterceptor)
        }

        clientBuilder
            .addInterceptor { chain ->
                val request = chain.request()
                val requestBuilder = request.newBuilder()
                    .addHeader(USER_AGENT, USER_AGENT_VALUE)
                    .addHeader(ACCEPT, ACCEPT_VALUE)

                chain.proceed(requestBuilder.build())
            }
            .authenticator(musicBrainzAuthenticator)
            // TODO: make sure when doing swipe to refresh, we actually fetch from network not this cache
            //  right now, it seems to work like that sometimes, switching between 200 and 304
            .cache(
                // TODO: how come we were able to use named parameters before?
                //  seems like we're only able to access "java" classes here whereas in app, we could access kotlin
                //  notice how we need to use url() vs url
                Cache(
                    File(context.cacheDir, "http_cache"),
                    // $0.05 worth of phone storage in 2020
                    50L * 1024L * 1024L // 50 MiB
                )
            )

        return clientBuilder.build()
    }
}
