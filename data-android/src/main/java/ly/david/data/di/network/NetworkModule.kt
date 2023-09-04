package ly.david.data.di.network

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
import javax.inject.Singleton
import ly.david.data.BuildConfig
import ly.david.data.common.network.ApiHttpClient
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.musicbrainz.MusicBrainzAuthState
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.musicbrainz.api.MusicBrainzOAuthApi
import ly.david.data.musicbrainz.api.MusicBrainzOAuthInfo
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.auth.SpotifyAuthApi
import ly.david.data.spotify.api.auth.SpotifyAuthState
import okhttp3.Cache
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(
        @ApplicationContext context: Context,
    ): HttpClient {
        return ApiHttpClient.configAndCreate(
            cache = Cache(
                directory = File(context.cacheDir, "ktor_okhttp_cache"),
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

    @Singleton
    @Provides
    fun provideCoverArtArchiveApi(
        httpClient: HttpClient,
    ): CoverArtArchiveApi = CoverArtArchiveApi.create(
        httpClient = httpClient,
    )

    @Singleton
    @Provides
    fun provideMusicBrainzOAuthApi(
        httpClient: HttpClient,
    ): MusicBrainzOAuthApi {
        return MusicBrainzOAuthApi.create(
            httpClient = httpClient,
        )
    }

    // TODO: pass repository instead of all these params
    @Singleton
    @Provides
    fun provideMusicBrainzApi(
        httpClient: HttpClient,
        musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
        musicBrainzOAuthApi: MusicBrainzOAuthApi,
        musicBrainzAuthState: MusicBrainzAuthState,
    ): MusicBrainzApi {
        return MusicBrainzApi.create(
            httpClient = httpClient,
            musicBrainzOAuthInfo = musicBrainzOAuthInfo,
            musicBrainzOAuthApi = musicBrainzOAuthApi,
            musicBrainzAuthState = musicBrainzAuthState,
        )
    }

    @Singleton
    @Provides
    fun provideSpotifyAuthApi(
        httpClient: HttpClient,
    ): SpotifyAuthApi {
        return SpotifyAuthApi.create(
            httpClient = httpClient,
        )
    }

    @Singleton
    @Provides
    fun provideSpotifyApi(
        httpClient: HttpClient,
        spotifyAuthState: SpotifyAuthState,
        spotifyAuthApi: SpotifyAuthApi,
    ): SpotifyApi {
        return SpotifyApi.create(
            httpClient = httpClient,
            clientId = BuildConfig.SPOTIFY_CLIENT_ID,
            clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET,
            spotifyAuthApi = spotifyAuthApi,
            spotifyAuthState = spotifyAuthState,
        )
    }
}
