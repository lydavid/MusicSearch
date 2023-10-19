package ly.david.musicsearch.data.musicbrainz.di

import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val musicBrainzDataModule = module {
    singleOf(::MusicBrainzAuthRepository) bind MusicBrainzAuthRepository::class
}
