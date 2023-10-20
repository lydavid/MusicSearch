package ly.david.musicsearch.data.musicbrainz.di

import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.auth.api.MusicBrainzOAuthApi
import ly.david.musicsearch.data.musicbrainz.auth.api.MusicBrainzOAuthApiImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val musicBrainzApiModule = module {
    single {
        MusicBrainzApi.create(
            httpClient = get(),
            musicBrainzAuthRepository = get(),
        )
    }
    singleOf(::MusicBrainzOAuthApiImpl) bind MusicBrainzOAuthApi::class
}
