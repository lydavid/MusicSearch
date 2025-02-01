package ly.david.musicsearch.data.musicbrainz.di

import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.CollectionApi
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzRepositoryImpl
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.musicbrainz.auth.api.MusicBrainzOAuthApi
import ly.david.musicsearch.data.musicbrainz.auth.api.MusicBrainzOAuthApiImpl
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val musicBrainzApiModule = module {

    single<MusicBrainzApi> {
        MusicBrainzApi.create(
            httpClient = get(),
            musicBrainzAuthRepository = get(),
        )
    } binds arrayOf(
        MusicBrainzApi::class,
        SearchApi::class,
        BrowseApi::class,
        LookupApi::class,
        CollectionApi::class,
        MusicBrainzUserApi::class,
    )

    singleOf(::MusicBrainzRepositoryImpl) bind MusicbrainzRepository::class
    singleOf(::MusicBrainzOAuthApiImpl) bind MusicBrainzOAuthApi::class
}
