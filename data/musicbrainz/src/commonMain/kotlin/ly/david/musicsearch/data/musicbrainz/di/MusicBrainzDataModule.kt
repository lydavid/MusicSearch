package ly.david.musicsearch.data.musicbrainz.di

import MusicSearch.data.musicbrainz.BuildConfig
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthRepository
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val musicBrainzDataModule = module {
    single {
        MusicBrainzOAuthInfo(
            clientId = BuildConfig.MUSICBRAINZ_CLIENT_ID,
            clientSecret = BuildConfig.MUSICBRAINZ_CLIENT_SECRET,
        )
    }
    singleOf(::MusicBrainzAuthRepository) bind MusicBrainzAuthRepository::class
}
