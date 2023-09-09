package ly.david.data.di.network

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import java.io.File
import ly.david.data.BuildConfig
import ly.david.data.common.network.ApiHttpClient
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.musicbrainz.api.MusicBrainzOAuthApi
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.auth.SpotifyOAuthApi
import okhttp3.Cache
import org.koin.dsl.module
import timber.log.Timber

//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkModule {
//
//    @Singleton
//    @Provides
//    fun provideHttpClient(
//        @ApplicationContext context: Context,
//    ): HttpClient {
//        return ApiHttpClient.configAndCreate(
//            cache = Cache(
//                directory = File(context.cacheDir, "ktor_okhttp_cache"),
//                maxSize = 50 * 1024 * 1024,
//            )
//        ) {
//            HttpResponseValidator {
//                handleResponseExceptionWithRequest { exception, _ ->
//                    handleRecoverableException(exception)
//                }
//            }
//
//            install(Logging) {
//                level = LogLevel.HEADERS
//                logger = object : Logger {
//                    override fun log(message: String) {
//                        Timber.d(message)
//                    }
//                }
//                sanitizeHeader { header -> header == HttpHeaders.Authorization }
//            }
//
//            install(HttpRequestRetry) {
//                retryOnExceptionOrServerErrors(maxRetries = 3)
//                exponentialDelay()
//            }
//        }
//    }
//
//    private suspend fun handleRecoverableException(exception: Throwable) {
//        when (exception) {
//            is NoTransformationFoundException -> {
//                Timber.e(exception)
//                throw RecoverableNetworkException("Requested json but got xml")
//            }
//
//            is ClientRequestException -> {
//                val exceptionResponse = exception.response
//                if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
//                    val exceptionResponseText = exceptionResponse.bodyAsText()
//                    throw RecoverableNetworkException(exceptionResponseText)
//                }
//            }
//        }
//    }
//
//    @Singleton
//    @Provides
//    fun provideCoverArtArchiveApi(
//        httpClient: HttpClient,
//    ): CoverArtArchiveApi = CoverArtArchiveApi.create(
//        httpClient = httpClient,
//    )
//
//    @Singleton
//    @Provides
//    fun provideMusicBrainzOAuthApi(
//        httpClient: HttpClient,
//    ): MusicBrainzOAuthApi {
//        return MusicBrainzOAuthApi.create(
//            httpClient = httpClient,
//        )
//    }
//
//    @Singleton
//    @Provides
//    fun provideMusicBrainzApi(
//        httpClient: HttpClient,
//        musicBrainzAuthRepository: MusicBrainzAuthRepository,
//    ): MusicBrainzApi {
//        return MusicBrainzApi.create(
//            httpClient = httpClient,
//            musicBrainzAuthRepository = musicBrainzAuthRepository,
//        )
//    }
//
//    @Singleton
//    @Provides
//    fun provideSpotifyOAuthApi(
//        httpClient: HttpClient,
//    ): SpotifyOAuthApi {
//        return SpotifyOAuthApi.create(
//            httpClient = httpClient,
//        )
//    }
//
//    @Singleton
//    @Provides
//    fun provideSpotifyApi(
//        httpClient: HttpClient,
//        spotifyAuthStore: SpotifyAuthStore,
//        spotifyOAuthApi: SpotifyOAuthApi,
//    ): SpotifyApi {
//        return SpotifyApi.create(
//            httpClient = httpClient,
//            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
//            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET,
//            spotifyOAuthApi = spotifyOAuthApi,
//            spotifyAuthStore = spotifyAuthStore,
//        )
//    }
//}

private suspend fun handleRecoverableException(exception: Throwable) {
    when (exception) {
        is NoTransformationFoundException -> {
            Timber.e(exception)
            throw RecoverableNetworkException("Requested json but got xml")
        }

        is ClientRequestException -> {
            val exceptionResponse = exception.response
            if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                val exceptionResponseText = exceptionResponse.bodyAsText()
                throw RecoverableNetworkException(exceptionResponseText)
            }
        }
    }
}

val networkModule = module {

    single<HttpClient> {
        ApiHttpClient.configAndCreate(
            cache = Cache(
                directory = File(get<Context>().cacheDir, "ktor_okhttp_cache"),
                maxSize = 50 * 1024 * 1024,
            )
        ) {
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    handleRecoverableException(exception)
                }
            }

            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }

            install(HttpRequestRetry) {
                retryOnExceptionOrServerErrors(maxRetries = 3)
                exponentialDelay()
            }
        }
    }



    single {
        CoverArtArchiveApi.create(
            httpClient = get(),
        )
    }

    single {
        MusicBrainzOAuthApi.create(
            httpClient = get(),
        )
    }

    single {
        MusicBrainzApi.create(
            httpClient = get(),
            musicBrainzAuthRepository = get(),
        )
    }

    single {
        SpotifyOAuthApi.create(
            httpClient = get(),
        )
    }

    single {
        SpotifyApi.create(
            httpClient = get(),
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET,
            spotifyOAuthApi = get(),
            spotifyAuthStore = get(),
        )
    }
}
