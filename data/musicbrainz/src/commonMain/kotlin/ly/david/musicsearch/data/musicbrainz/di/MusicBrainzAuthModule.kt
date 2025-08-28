package ly.david.musicsearch.data.musicbrainz.di

import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzLogoutImpl
import ly.david.musicsearch.shared.domain.auth.usecase.MusicBrainzLogout
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val musicBrainzAuthModule: Module = module {
    includes(musicBrainzAuthPlatformModule)

    singleOf(::MusicBrainzLogoutImpl) bind MusicBrainzLogout::class
}
