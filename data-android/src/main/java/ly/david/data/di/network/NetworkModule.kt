package ly.david.data.di.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import javax.inject.Singleton
import ly.david.data.BuildConfig
import ly.david.data.core.network.ApiHttpClient
import ly.david.data.core.network.RecoverableNetworkException
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.network.MusicBrainzAuthState
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.spotify.api.SpotifyApi
import ly.david.data.spotify.api.auth.SpotifyAuthApi
import ly.david.data.spotify.api.auth.SpotifyAuthState

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return ApiHttpClient.configAndCreate {
            HttpResponseValidator {
                handleResponseExceptionWithRequest { exception, _ ->
                    handleRecoverableException(exception)
                }
            }
        }
    }

    private suspend fun handleRecoverableException(exception: Throwable) {
        when (exception) {
            is NoTransformationFoundException -> {
                // TODO: should be logged
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
    fun provideMusicBrainzApi(
        httpClient: HttpClient,
        musicBrainzAuthState: MusicBrainzAuthState,
    ): MusicBrainzApi {
        return MusicBrainzApi.create(
            httpClient = httpClient,
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
