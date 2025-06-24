package ly.david.musicsearch.shared.di

import ly.david.musicsearch.shared.feature.nowplaying.nowPlayingFeatureModule
import ly.david.musicsearch.shared.feature.spotify.spotifyFeatureModule
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    includes(
        nowPlayingFeatureModule,
        spotifyFeatureModule,
    )
}
