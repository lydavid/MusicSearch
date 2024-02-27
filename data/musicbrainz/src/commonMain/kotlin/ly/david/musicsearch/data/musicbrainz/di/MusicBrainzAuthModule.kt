package ly.david.musicsearch.data.musicbrainz.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ly.david.musicsearch.data.musicbrainz.auth.Logout

val musicBrainzAuthModule: Module = module {
    includes(musicBrainzAuthPlatformModule)
    singleOf(::Logout)
}
